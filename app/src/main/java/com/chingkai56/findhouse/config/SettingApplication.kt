package com.chingkai56.findhouse.config

import android.app.Application
import com.chingkai56.findhouse.Database
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
    }

    companion object{
        private lateinit var instance:SettingApplication
        fun getDb() = instance.database
    }
}