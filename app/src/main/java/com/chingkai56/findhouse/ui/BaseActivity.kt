package com.chingkai56.findhouse.ui

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import timber.log.Timber

/**
 * Created by timhuang on 2020/11/5.
 **/

abstract class BaseActivity: AppCompatActivity(),OnHouseAction {

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    override fun seeHouseProfile(intent: Intent) {
        val activities = packageManager?.queryIntentActivities(intent,0) ?: listOf()
        if (activities.isNotEmpty()){
            startActivity(intent)
        }else{
            Timber.e("no browser")
            Toast.makeText(this, "no browser", Toast.LENGTH_SHORT).show()
        }
    }
}