package com.marina.servicestest

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.app.job.JobWorkItem
import android.content.ComponentName
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import com.marina.servicestest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private var page = 0

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

        binding.jobScheduler.setOnClickListener {
            // указываем, какой именно сервис нам нужен
            val componentName = ComponentName(this, MyJobService::class.java)

            // только на устройстве, которое заряжается, подключено к wifi
            val jobInfo = JobInfo.Builder(MyJobService.JOB_ID, componentName)
                .setRequiresCharging(true)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .build()

            val jobScheduler = getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val intent = MyJobService.newIntent(page++)
                jobScheduler.enqueue(jobInfo, JobWorkItem(intent))
            } else {
                startService(MyIntentService2.newIntent(this, page++))
            }
        }

        binding.jobIntentService.setOnClickListener {
            MyJobIntentService.enqueue(this, page++)
        }

        binding.workManager.setOnClickListener {
            // лучше передавать контекст всего приложения,
            // чтобы не было утечек памяти, когда активити уже не существует,
            // а workManager еще работает
            val workManager = WorkManager.getInstance(applicationContext)

            /*
            * второй параметр отвечает за действия при запуске работы,
            *  которая уже была запущена
            *       APPEND - новый положен в очередь,
            * если ошибка, она распространится на все сервисы в очереди
            *       REPLACE - старый заменяется новым
            *       APPEND_OR_REPLACE - новый положен в очередь,
            * в случае ошибки создается новая цепочка
            *       KEEP - старый продолжает работу, новый игнорируется
            * */
            workManager.enqueueUniqueWork(
                MyWorker.WORK_NAME,
                ExistingWorkPolicy.APPEND,
                MyWorker.makeRequest(page++)
            )
        }

    }
}