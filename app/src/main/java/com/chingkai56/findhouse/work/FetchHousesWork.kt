package com.chingkai56.findhouse.work

import android.app.NotificationManager
import android.content.Context
import androidx.core.content.ContextCompat
import androidx.work.*
import com.chingkai56.findhouse.config.BaseApplication
import com.chingkai56.findhouse.data.repository.HouseRepository
import com.chingkai56.findhouse.helper.notifyNew
import timber.log.Timber
import java.util.concurrent.TimeUnit

/**
 * Created by timhuang on 2020/10/14.
 **/

class FetchHousesWork(context: Context, workerParams: WorkerParameters) : CoroutineWorker(context, workerParams) {
    private val repository = HouseRepository.getInstance(context)
    override suspend fun doWork(): Result {
        Timber.e("start to work...")
        val shouldNotify = repository.fetch()
        if (shouldNotify){
            sendNotification()
        }
        return Result.success()
    }

    private fun sendNotification() {
        val notificationManager = ContextCompat.getSystemService(applicationContext, NotificationManager::class.java) as NotificationManager
        notificationManager.notifyNew(applicationContext)
    }

    companion object{
        fun startWork(context: Context){
            Timber.e("start work at background...")
            val constraints = Constraints.Builder()
                    .setRequiresBatteryNotLow(true)
                    .build()

            val request : PeriodicWorkRequest = PeriodicWorkRequestBuilder<FetchHousesWork>(
                    30, TimeUnit.MINUTES,
                    30, TimeUnit.MINUTES)
                    .setConstraints(constraints)
                    .build()
            //use unique work since this method might get trigger several times,and just keep the first one
            WorkManager
                    .getInstance(context)
                    .enqueueUniquePeriodicWork(BaseApplication.CONTINUE_FETCH, ExistingPeriodicWorkPolicy.KEEP,request)
        }

        fun cancelWork(context: Context){
            val operation = WorkManager
                    .getInstance(context)
                    .cancelUniqueWork(BaseApplication.CONTINUE_FETCH)
            operation.result.addListener({
                                         Timber.e("work cancel success")
            }) { command -> Thread(command).run() }
        }
    }

}