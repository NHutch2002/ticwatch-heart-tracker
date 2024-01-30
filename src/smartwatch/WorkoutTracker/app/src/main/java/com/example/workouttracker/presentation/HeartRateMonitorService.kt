package com.example.workouttracker.presentation

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.os.PowerManager
import androidx.core.app.NotificationCompat
import com.example.testapplication.R

class HeartRateMonitorService : Service(){
    private var wakeLock: PowerManager.WakeLock? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val channelId = "HeartRateMonitorService"
        val channelName = "Heart Rate Monitor Service"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelId, channelName, importance)


        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)

        val notification: Notification = NotificationCompat.Builder(this, "HeartRateMonitorService")
            .setContentTitle("Heart Rate Recovery")
            .setContentText("Monitoring Heart Rate...")
            .setSmallIcon(R.drawable.flame_icon)
            .build()

        startForeground(1, notification)

        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "HeartRateMonitorService::lock").apply {
            acquire()
        }

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        wakeLock?.let {
            if (it.isHeld) {
                it.release()
            }
        }
    }
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}