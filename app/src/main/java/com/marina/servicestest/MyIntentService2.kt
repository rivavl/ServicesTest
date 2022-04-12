package com.marina.servicestest

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.util.Log

/* устаревший класс
*
* отличия от обычного сервиса:
*   наследуется от IntentService
*   onHandleIntent вместо onStartCommand; код выполняется в другом потоке
*   одновременно работает только один сервис
* */
class MyIntentService2 : IntentService(NAME) {

    //вызывается при создании сервиса
    override fun onCreate() {
        super.onCreate()
        log("onCreate")

        // true -  как в обычном сервисе вернуть START_REDELIVER_INTENT
        // false - START_NOT_STICKY
        setIntentRedelivery(true)
    }

    override fun onHandleIntent(intent: Intent?) {
        log("onStartCommand")
        val page = intent?.getIntExtra(PAGE, 0)
        for (i in 0 until 100) {
            Thread.sleep(1000L)
            log("Timer $i $page")
        }
    }

    //вызывается при уничтожении сервиса
    override fun onDestroy() {
        super.onDestroy()
        log("onDestroy")
    }

    private fun log(message: String) {
        Log.d("SERVICE_TAG", "MyIntentService: $message")
    }

    companion object {

        private const val NAME = "MyIntentService"
        private const val PAGE = "page"

        fun newIntent(context: Context, page: Int): Intent {
            return Intent(context, MyIntentService2::class.java).apply {
                putExtra(PAGE, page)
            }
        }
    }
}