package com.mindscape.app.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.mindscape.app.R

/**
 * Native Offline Notification Manager for MindScape Energy & Mood Check-In (FR-02 & FR-05).
 * Generates interactive custom RemoteViews that adhere strictly to the 60-30-10 color balance.
 */
object CheckInNotificationManager {

    const val CHANNEL_ID = "mindscape_checkin_channel"
    const val CHANNEL_NAME = "Daily Energy & Mood Check-In"
    const val NOTIFICATION_ID = 1001

    // Mood Labels & Icons
    val MOOD_LABELS = mapOf(
        1 to "Drained",
        2 to "Low",
        3 to "Balanced",
        4 to "Good",
        5 to "Thriving"
    )

    // Energy Labels
    val ENERGY_LABELS = mapOf(
        1 to "Level 1 • Exhausted",
        2 to "Level 2 • Low",
        3 to "Level 3 • Moderate",
        4 to "Level 4 • Energized",
        5 to "Level 5 • Vibrant"
    )

    /**
     * Creates and registers the high-importance Notification Channel (Android 8.0+).
     */
    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Interactive daily prompt for offline mood & energy logging."
                enableLights(true)
                lightColor = ContextCompat.getColor(context, R.color.mind_primary_accent)
                enableVibration(true)
                setShowBadge(true)
            }
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    /**
     * Builds and displays the interactive Rich Notification with selected state.
     *
     * @param context Android Application Context
     * @param selectedMood Selected mood score (1 to 5, default = 4 "Good")
     * @param selectedEnergy Selected energy level (1 to 5, default = 4 "Energized")
     */
    fun showCheckInNotification(
        context: Context,
        selectedMood: Int = 4,
        selectedEnergy: Int = 4
    ) {
        createNotificationChannel(context)

        val expandedView = buildExpandedRemoteViews(context, selectedMood, selectedEnergy)
        val collapsedView = buildCollapsedRemoteViews(context, selectedMood, selectedEnergy)

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_stat_mindscape)
            .setColor(ContextCompat.getColor(context, R.color.mind_primary_accent))
            .setCustomContentView(collapsedView)
            .setCustomBigContentView(expandedView)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setAutoCancel(false)
            .setOngoing(false)
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(NOTIFICATION_ID, notification)
    }

    /**
     * Builds the expanded RemoteViews UI matching the screenshot layout.
     */
    private fun buildExpandedRemoteViews(
        context: Context,
        selectedMood: Int,
        selectedEnergy: Int
    ): RemoteViews {
        val views = RemoteViews(context.packageName, R.layout.notification_checkin_expanded)

        val moodLabel = MOOD_LABELS[selectedMood] ?: "Good"
        val energyLabel = ENERGY_LABELS[selectedEnergy] ?: "Level 4 • Energized"

        // Set dynamic state labels
        views.setTextViewText(R.id.tv_selected_mood_label, moodLabel)
        views.setTextViewText(R.id.tv_selected_energy_label, energyLabel)

        // Highlight selected mood column (60-30-10 rule: 10% accent on selected item)
        val moodButtonIds = listOf(
            R.id.btn_mood_1,
            R.id.btn_mood_2,
            R.id.btn_mood_3,
            R.id.btn_mood_4,
            R.id.btn_mood_5
        )
        val moodTextIds = listOf(
            R.id.tv_mood_name_1,
            R.id.tv_mood_name_2,
            R.id.tv_mood_name_3,
            R.id.tv_mood_name_4,
            R.id.tv_mood_name_5
        )

        for (i in 1..5) {
            val btnId = moodButtonIds[i - 1]
            val textId = moodTextIds[i - 1]
            val isSelected = (i == selectedMood)

            views.setInt(
                btnId,
                "setBackgroundResource",
                if (isSelected) R.drawable.bg_mood_selected else R.drawable.bg_mood_unselected
            )
            views.setTextColor(
                textId,
                ContextCompat.getColor(
                    context,
                    if (isSelected) R.color.mind_primary_accent else R.color.mind_text_secondary
                )
            )

            // Attach PendingIntent to switch mood
            val moodIntent = Intent(context, CheckInNotificationReceiver::class.java).apply {
                action = CheckInNotificationReceiver.ACTION_SELECT_MOOD
                putExtra(CheckInNotificationReceiver.EXTRA_MOOD_SCORE, i)
                putExtra(CheckInNotificationReceiver.EXTRA_ENERGY_LEVEL, selectedEnergy)
            }
            val pendingMoodIntent = PendingIntent.getBroadcast(
                context,
                100 + i,
                moodIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            views.setOnClickPendingIntent(btnId, pendingMoodIntent)
        }

        // Attach PendingIntents for Energy buttons (1..5)
        val energyButtonIds = listOf(
            R.id.btn_energy_1,
            R.id.btn_energy_2,
            R.id.btn_energy_3,
            R.id.btn_energy_4,
            R.id.btn_energy_5
        )
        for (i in 1..5) {
            val btnId = energyButtonIds[i - 1]
            val energyIntent = Intent(context, CheckInNotificationReceiver::class.java).apply {
                action = CheckInNotificationReceiver.ACTION_SELECT_ENERGY
                putExtra(CheckInNotificationReceiver.EXTRA_MOOD_SCORE, selectedMood)
                putExtra(CheckInNotificationReceiver.EXTRA_ENERGY_LEVEL, i)
            }
            val pendingEnergyIntent = PendingIntent.getBroadcast(
                context,
                200 + i,
                energyIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            views.setOnClickPendingIntent(btnId, pendingEnergyIntent)
        }

        // Later Button PendingIntent (Dismisses notification)
        val laterIntent = Intent(context, CheckInNotificationReceiver::class.java).apply {
            action = CheckInNotificationReceiver.ACTION_DISMISS
        }
        val pendingLaterIntent = PendingIntent.getBroadcast(
            context,
            301,
            laterIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        views.setOnClickPendingIntent(R.id.btn_action_later, pendingLaterIntent)

        // Mic Button PendingIntent
        val micIntent = Intent(context, CheckInNotificationReceiver::class.java).apply {
            action = CheckInNotificationReceiver.ACTION_MIC_TAP
        }
        val pendingMicIntent = PendingIntent.getBroadcast(
            context,
            302,
            micIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        views.setOnClickPendingIntent(R.id.btn_action_mic, pendingMicIntent)

        // Log Check-In Button PendingIntent (Persists to local Room DB offline)
        val logIntent = Intent(context, CheckInNotificationReceiver::class.java).apply {
            action = CheckInNotificationReceiver.ACTION_LOG_CHECKIN
            putExtra(CheckInNotificationReceiver.EXTRA_MOOD_SCORE, selectedMood)
            putExtra(CheckInNotificationReceiver.EXTRA_ENERGY_LEVEL, selectedEnergy)
        }
        val pendingLogIntent = PendingIntent.getBroadcast(
            context,
            303,
            logIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        views.setOnClickPendingIntent(R.id.btn_action_log, pendingLogIntent)

        return views
    }

    /**
     * Builds the compact collapsed RemoteViews UI.
     */
    private fun buildCollapsedRemoteViews(
        context: Context,
        selectedMood: Int,
        selectedEnergy: Int
    ): RemoteViews {
        val views = RemoteViews(context.packageName, R.layout.notification_checkin_collapsed)

        val logIntent = Intent(context, CheckInNotificationReceiver::class.java).apply {
            action = CheckInNotificationReceiver.ACTION_LOG_CHECKIN
            putExtra(CheckInNotificationReceiver.EXTRA_MOOD_SCORE, selectedMood)
            putExtra(CheckInNotificationReceiver.EXTRA_ENERGY_LEVEL, selectedEnergy)
        }
        val pendingLogIntent = PendingIntent.getBroadcast(
            context,
            401,
            logIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        views.setOnClickPendingIntent(R.id.btn_collapsed_quick_log, pendingLogIntent)

        return views
    }

    /**
     * Cancels the notification after successful logging or user dismiss.
     */
    fun cancelNotification(context: Context) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.cancel(NOTIFICATION_ID)
    }
}
