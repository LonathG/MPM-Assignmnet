package com.mindscape.app.domain.engine

import android.content.Context
import android.content.res.AssetFileDescriptor
import android.util.Log
import com.mindscape.app.domain.model.EnergyLevel
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.io.IOException
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

/**
 * Native Offline TensorFlow Lite Engine for Smart Habit Adaptation (FR-03).
 * Loads [habit_adapter.tflite] directly from Android assets and runs on-device inference.
 */
class HabitAdaptationEngine(private val context: Context) : AutoCloseable {

    companion object {
        private const val TAG = "HabitAdaptationEngine"
        private const val MODEL_NAME = "habit_adapter.tflite"

        // Adaptation classes:
        // 0 -> Reduced / Light adaptation (e.g., 33% intensity)
        // 1 -> Moderate adaptation (e.g., 50% - 66% intensity)
        // 2 -> Full / Normal target (100% intensity)
        const val CLASS_LIGHT = 0
        const val CLASS_MODERATE = 1
        const val CLASS_FULL = 2
    }

    private var interpreter: Interpreter? = null
    private var outputClassCount: Int = 3

    init {
        initInterpreter()
    }

    /**
     * Initializes the TFLite Interpreter using MappedByteBuffer.
     */
    private fun initInterpreter() {
        try {
            val modelBuffer = loadModelFile(context, MODEL_NAME)
            val options = Interpreter.Options().apply {
                setNumThreads(2)
                setUseNNAPI(false)
            }
            val loadedInterpreter = Interpreter(modelBuffer, options)
            
            // Inspect output tensor shape to handle model dynamically
            val outputShape = loadedInterpreter.getOutputTensor(0).shape()
            if (outputShape.isNotEmpty()) {
                outputClassCount = outputShape.last().coerceAtLeast(1)
            }
            
            interpreter = loadedInterpreter
            Log.d(TAG, "Successfully loaded $MODEL_NAME (Output dimensions: $outputClassCount)")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to load TFLite model from assets: $MODEL_NAME", e)
        }
    }

    /**
     * Memory maps the model file from Android assets without unzipping.
     */
    @Throws(IOException::class)
    private fun loadModelFile(context: Context, filename: String): MappedByteBuffer {
        val fileDescriptor: AssetFileDescriptor = context.assets.openFd(filename)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength).also {
            inputStream.close()
            fileDescriptor.close()
        }
    }

    /**
     * Predicts the habit adaptation class from raw float features.
     *
     * @param energy Float value representing user's energy (e.g. 1.0 = Low, 2.0 = Medium, 3.0 = High)
     * @param mood Float value representing user's mood score (e.g. 1.0 to 5.0)
     * @param baseDuration Float value representing base habit target / duration
     * @return Integer representing the predicted adaptation class (0 = Light, 1 = Moderate, 2 = Full)
     */
    @Synchronized
    fun predictAdaptationClass(
        energy: Float,
        mood: Float,
        baseDuration: Float
    ): Int {
        val activeInterpreter = interpreter ?: run {
            Log.w(TAG, "Interpreter not ready. Falling back to heuristic rule.")
            return fallbackPrediction(energy, mood)
        }

        try {
            // Input shape: [1, 3]
            val input = arrayOf(floatArrayOf(energy, mood, baseDuration))

            if (outputClassCount == 1) {
                // Single regression or single class index output
                val output = Array(1) { FloatArray(1) }
                activeInterpreter.run(input, output)
                val rawValue = output[0][0]
                return rawValue.toInt().coerceIn(CLASS_LIGHT, CLASS_FULL)
            } else {
                // Multi-class probability distribution [1, outputClassCount]
                val output = Array(1) { FloatArray(outputClassCount) }
                activeInterpreter.run(input, output)
                val probs = output[0]
                return getArgMax(probs)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error executing TFLite inference", e)
            return fallbackPrediction(energy, mood)
        }
    }

    /**
     * Overload helper method to predict directly using domain models.
     */
    fun predict(energyLevel: EnergyLevel, moodScore: Int, baseTargetValue: Int): Int {
        val energyFloat = when (energyLevel) {
            EnergyLevel.LOW -> 1.0f
            EnergyLevel.MEDIUM -> 2.0f
            EnergyLevel.HIGH -> 3.0f
        }
        val moodFloat = moodScore.toFloat()
        val durationFloat = baseTargetValue.toFloat()
        return predictAdaptationClass(energyFloat, moodFloat, durationFloat)
    }

    /**
     * Find index of maximum value (ArgMax).
     */
    private fun getArgMax(array: FloatArray): Int {
        if (array.isEmpty()) return CLASS_FULL
        var maxIndex = 0
        var maxValue = array[0]
        for (i in 1 until array.size) {
            if (array[i] > maxValue) {
                maxValue = array[i]
                maxIndex = i
            }
        }
        return maxIndex
    }

    /**
     * Offline fallback if the model file is inaccessible or fails to run.
     */
    private fun fallbackPrediction(energy: Float, mood: Float): Int {
        return when {
            mood <= 2.0f || energy <= 1.0f -> CLASS_LIGHT
            mood == 3.0f || energy == 2.0f -> CLASS_MODERATE
            else -> CLASS_FULL
        }
    }

    override fun close() {
        interpreter?.close()
        interpreter = null
        Log.d(TAG, "HabitAdaptationEngine interpreter closed.")
    }
}
