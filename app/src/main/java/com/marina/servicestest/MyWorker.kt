package com.marina.servicestest

import android.content.Context
import android.util.Log
import androidx.work.*

// не нужно регестрировать в манифесте
class MyWorker(
    context: Context,
    private val workerParameters: WorkerParameters
) : Worker(context, workerParameters) {


    /* выполняется в другом потоке
    *  возвращает
    *       Result.success(все хорошо),
    *       Result.failure(ошибка, не перезапускается),
    *       Result.retry(ошибка, перезапускается)
    * */
    override fun doWork(): Result {
        log("doWork")
        val page = workerParameters.inputData.getInt(PAGE, 0)
        for (i in 0 until 100) {
            Thread.sleep(1000L)
            log("Timer $i $page")
        }
        return Result.success()
    }

    private fun log(message: String) {
        Log.d("SERVICE_TAG", "MyWorker: $message")
    }

    companion object {
        private const val PAGE = "page"
        const val WORK_NAME = "work name"

        fun makeRequest(page: Int): OneTimeWorkRequest {
            return OneTimeWorkRequestBuilder<MyWorker>().apply {
                setInputData(workDataOf(PAGE to page))
                setConstraints(makeConstraints())
            }.build()
        }

        private fun makeConstraints() = Constraints.Builder()
            .setRequiresCharging(true)
            .build()
    }
}