package com.example.serviceapp

import android.app.Activity
import android.app.ActivityManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.serviceapp.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        createNotificationChannel()



        binding.startServiceButton.setOnClickListener {
            if (!isServiceRunning(MyService::class.java)){
                val serviceIntent = Intent(this, MyService::class.java)
                ContextCompat.startForegroundService(this,serviceIntent)
                Toast.makeText(this,"Service Started",Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this,"Service Already Running",Toast.LENGTH_SHORT).show()
                Log.d("startServiceButton", "service already running")
            }
        }

        binding.stopServiceButton.setOnClickListener {
            if (!isServiceRunning(MyService::class.java)){
                Toast.makeText(this,"Service Not Running",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val serviceIntent = Intent(this, MyService::class.java)
            stopService(serviceIntent)
            Toast.makeText(this,"Service Stopped",Toast.LENGTH_SHORT).show()
        }

    }

    private fun createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val serviceChannel = NotificationChannel(
                "my_channel_id",
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
                )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }

    private fun isServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }

}