package com.chingkai56.findhouse.config

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import android.os.Build
import androidx.work.*
import com.chingkai56.findhouse.Database
import com.chingkai56.findhouse.R
import com.chingkai56.findhouse.work.FetchHousesWork
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver

/**
 * Created by timhuang on 2020/10/13.
 **/

class SettingApplication:Application() {

    private lateinit var driver:SqlDriver
    private lateinit var database: Database


    override fun onCreate() {
        super.onCreate()
        instance = this
        driver = AndroidSqliteDriver(Database.Schema,this,"test.db")
        database = Database.invoke(driver)

        createChannel()
        val constraints = Constraints.Builder()
                .setRequiresBatteryNotLow(true)
                .build()

        val request : PeriodicWorkRequest = PeriodicWorkRequestBuilder<FetchHousesWork>(
                30,java.util.concurrent.TimeUnit.MINUTES,
                30,java.util.concurrent.TimeUnit.MINUTES)
                .setConstraints(constraints)
                .build()
        WorkManager
                .getInstance(this)
                .enqueueUniquePeriodicWork(CONTINUE_FETCH,ExistingPeriodicWorkPolicy.REPLACE,request)
    }

    fun db(): Database {
        return database
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val notificationManager = getSystemService(NotificationManager::class.java)

            val defaultChannel = NotificationChannel(
                    getString(R.string.default_notification_channel_id),
                    getString(R.string.default_channel_name),
                    NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                setShowBadge(true)
                lightColor = Color.RED
                description = getString(R.string.default_channel_description)
            }

            notificationManager?.createNotificationChannel(defaultChannel)
        }
    }

    companion object{
        private lateinit var instance:SettingApplication
        @JvmStatic
        fun getDb() = instance.db()
        const val CONTINUE_FETCH = "continue fetch"
    }
}