package com.marina.servicestest

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.*

// нужно зарегестрировать в манифесте   
class MyService : Service() {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    //вызывается при создании сервиса
    override fun onCreate() {
        super.onCreate()
        log("onCreate")
    }

    //вся работа выполняется здесь; метод работает В ГЛАВНОМ ПОТОКЕ
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        log("onStartCommand")
        coroutineScope.launch {
            for (i in 0 until 100) {
                delay(1000L)
                log("Timer $i")
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    //вызывается при уничтожении сервиса
    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
        log("onDestroy")
    }

    private fun log(message: String) {
        Log.d("SERVICE_TAG", "MyService: $message")
    }

    companion object {

        fun newIntent(context: Context): Intent {
            return Intent(context, MyService::class.java)
        }
    }
}