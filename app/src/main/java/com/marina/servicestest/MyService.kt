package com.marina.servicestest

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.*

/* нужно зарегестрировать в манифесте
* с 26 проблемы с сервисами;
* при пересоздании onStartCommand не вызывается, сразу onDestroy
* нужно уведомлять пользователя о работе в фоне(на протяжении всего времени )
* */
class MyService : Service() {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    //вызывается при создании сервиса
    override fun onCreate() {
        super.onCreate()
        log("onCreate")
    }

    /* вся работа выполняется здесь; метод работает В ГЛАВНОМ ПОТОКЕ
    *
    * возвращает:
    *   START_STICKY - если система убила сервис, он будет пересоздан (intent == null)
    *   START_NOT_STICKY - сервис не перезапускается
    *   START_REDELIVER_INTENT - пересоздается, intent != null
    *  */
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        log("onStartCommand")
        val start = intent?.getIntExtra(EXTRA_START, 0) ?: 0
        coroutineScope.launch {
            for (i in start until start + 100) {
                delay(1000L)
                log("Timer $i")
            }
        }
        return START_REDELIVER_INTENT
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

        private const val EXTRA_START = "start"

        fun newIntent(context: Context, start: Int): Intent {
            return Intent(context, MyService::class.java).apply {
                putExtra(EXTRA_START, start)
            }
        }
    }
}