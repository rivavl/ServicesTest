package com.marina.servicestest

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.JobIntentService

// отличается от обычного интент сервиса только методом onHandleWork
class MyJobIntentService : JobIntentService() {

    //вызывается при создании сервиса
    override fun onCreate() {
        super.onCreate()
        log("onCreate")
    }

    //вызывается при уничтожении сервиса
    override fun onDestroy() {
        super.onDestroy()
        log("onDestroy")
    }

    override fun onHandleWork(intent: Intent) {
        log("onStartCommand")
        val page = intent.getIntExtra(PAGE, 0)
        for (i in 0 until 100) {
            Thread.sleep(1000L)
            log("Timer $i $page")
        }
    }

    private fun log(message: String) {
        Log.d("SERVICE_TAG", "MyJobIntentService: $message")
    }

    companion object {

        private const val PAGE = "page"
        private const val JOB_ID = 111

        fun enqueue(context: Context, page: Int) {
            enqueueWork(
                context,
                MyJobIntentService::class.java,
                JOB_ID,
                newIntent(context, page)
            )
        }

        private fun newIntent(context: Context, page: Int): Intent {
            return Intent(context, MyJobIntentService::class.java).apply {
                putExtra(PAGE, page)
            }
        }
    }
}