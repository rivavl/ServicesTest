package com.marina.servicestest

import android.app.IntentService
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat

/* устаревший класс
*
* отличия от обычного сервиса:
*   наследуется от IntentService
*   onHandleIntent вместо onStartCommand; код выполняется в другом потоке
*   одновременно работает только один сервис
* */
class MyIntentService : IntentService(NAME) {

    //вызывается при создании сервиса
    override fun onCreate() {
        super.onCreate()
        log("onCreate")

        // true -  как в обычном сервисе вернуть START_REDELIVER_INTENT
        // false - START_NOT_STICKY
        setIntentRedelivery(true)
        createNotificationChannel()
        // метод для показа уведомления
        startForeground(NOTIFICATION_ID, createNotification())
    }

    override fun onHandleIntent(p0: Intent?) {
        log("onStartCommand")
        for (i in 0 until 100) {
            Thread.sleep(1000L)
            log("Timer $i")
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

    private fun createNotificationChannel() {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        // с апи 26 нужно создавать канал для уведомлений
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    private fun createNotification() = NotificationCompat.Builder(this, CHANNEL_ID)
        .setContentTitle("Title")
        .setContentText("Text")
        .setSmallIcon(R.drawable.ic_launcher_background)
        .build()

    companion object {
        private const val CHANNEL_ID = "channel_id"
        private const val CHANNEL_NAME = "channel_name"
        private const val NOTIFICATION_ID = 1
        private const val NAME = "MyIntentService"

        fun newIntent(context: Context): Intent {
            return Intent(context, MyIntentService::class.java)
        }
    }
}