package com.example.serviceapp

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import androidx.core.app.NotificationCompat
import android.util.Log
import android.widget.Toast

class MyService : Service() {

    private val TAG = "MyService"
    private var count = 0
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var runnable: Runnable

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "Service created")
        runnable = Runnable {
            count++
            Log.d(TAG, "Count: $count")
            Toast.makeText(applicationContext,"Service Running : Count: $count", Toast.LENGTH_SHORT).show()
            handler.postDelayed(runnable, 30000) // Repeat task every 30 second
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "Service started")
        startForeground(1, createNotification())
        handler.post(runnable) // Start the counting task
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(runnable) // Stop the task
        Log.d(TAG, "Service destroyed")
    }

    private fun createNotification(): Notification {
        return NotificationCompat.Builder(this, "my_channel_id")
            .setContentTitle("Foreground Service")
            .setContentText("Service is running")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
    }
}
