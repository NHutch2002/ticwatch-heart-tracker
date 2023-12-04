package com.example.workouttracker.presentation

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.testapplication.R

class HeartRateMonitorService : Service(){

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

        val viewModel = HeartRateMonitorViewModel(application = application)

        viewModel.startHeartRateMonitoring(this, maxHeartRate =150f)

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


}