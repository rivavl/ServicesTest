package com.marina.servicestest

import android.app.Service
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.*

/*
*  наследуется от JobService
*  надо переопределить onStartJob и onStopJob
* */
class MyJobService : JobService() {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    //вызывается при создании сервиса
    override fun onCreate() {
        super.onCreate()
        log("onCreate")
    }

    /*
    * вызывается при старте сервиса
    * код внутри метода может быть синхронным
    * возвращаемое значение - выполняется работа или уже нет
    * при асинхронный операциях возвращаем true
    * */

    override fun onStartJob(params: JobParameters?): Boolean {
        log("onStartJob")
        coroutineScope.launch {
            for (i in 0 until 100) {
                delay(1000L)
                log("Timer $i")
            }
            // остановка сервиса, второй параметр овечает за перезапуск через какое-то время
            jobFinished(params, true)
        }
        // если продолжает выполняться - true
        // тогда останавливаем вручную при помощи jobFinished
        return true
    }

    // вызывается, если сервис был остановлен системой
    // если останавливаем с помощью jobFinished, onStopJob не вызывается
    override fun onStopJob(p0: JobParameters?): Boolean {
        log("onStopJob")
        // нужно перезапустить - true
        return true
    }

    //вызывается при уничтожении сервиса
    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
        log("onDestroy")
    }

    private fun log(message: String) {
        Log.d("SERVICE_TAG", "MyJobService: $message")
    }

    companion object {
        const val JOB_ID = 111
    }
}