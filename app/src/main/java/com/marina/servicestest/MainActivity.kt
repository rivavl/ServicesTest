package com.marina.servicestest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.marina.servicestest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.simpleService.setOnClickListener {
            // остановка сервиса снаружи
            stopService(MyForegroundService.newIntent(this))
            startService(MyService.newIntent(this, 25))
        }

        binding.foregroundService.setOnClickListener {
            // в методе startForegroundService мы обещаем,
            // что в течении 5 секунд после старта сервиса, покажем уведомление
            ContextCompat.startForegroundService(
                this,
                MyForegroundService.newIntent(this)
            )
        }

        binding.intentService.setOnClickListener {
            // в методе startForegroundService мы обещаем,
            // что в течении 5 секунд после старта сервиса, покажем уведомление
            ContextCompat.startForegroundService(
                this,
                MyIntentService.newIntent(this)
            )
        }

    }
}