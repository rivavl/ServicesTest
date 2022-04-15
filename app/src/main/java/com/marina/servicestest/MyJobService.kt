package com.marina.servicestest

import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Intent
import android.os.Build
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            coroutineScope.launch {
                var workItem = params?.dequeueWork()
                while (workItem != null) {
                    val page = workItem.intent.getIntExtra(PAGE, 0)

                    for (i in 0 until 5) {
                        delay(1000L)
                        log("Timer $i $page")
                    }
                    // завершение только текущего сервиса(элемента очереди)
                    params?.completeWork(workItem)
                    workItem = params?.dequeueWork()
                }
                // остановка всего сервиса, второй параметр овечает за перезапуск через какое-то время
                jobFinished(params, false)
            }
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

        private const val PAGE = "page"

        // PersistableBundle - только примитивы и строки
        fun newIntent(page: Int): Intent {
            return Intent().apply {
                putExtra(PAGE, page)
            }
        }
    }
}