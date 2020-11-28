package com.chingkai56.findhouse.work

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.chingkai56.findhouse.R
import com.chingkai56.findhouse.data.repository.HouseRepository
import com.chingkai56.findhouse.ui.HouseListActivity
import timber.log.Timber

/**
 * Created by timhuang on 2020/10/14.
 **/

class FetchHousesWork(context: Context, workerParams: WorkerParameters) : CoroutineWorker(context, workerParams) {
    private val repository = HouseRepository()
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

        val intent = Intent(applicationContext,HouseListActivity::class.java)
        val pi = PendingIntent.getActivity(
                applicationContext,
                123,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        )

        //TODO change this after multiple push are needed
        val notificationId = 1234
        val builder = NotificationCompat.Builder(
                applicationContext,
                applicationContext.getString(R.string.default_notification_channel_id)
        )
                .setColor(ResourcesCompat.getColor(applicationContext.resources,R.color.colorAccent,null))
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("有新的物件")
                .setContentText("點擊查看")
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pi)

        notificationManager.notify(notificationId, builder.build())
    }


}