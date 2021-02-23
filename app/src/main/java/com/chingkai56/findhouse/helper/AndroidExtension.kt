package com.chingkai56.findhouse.helper

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.widget.TextView
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.chingkai56.findhouse.R
import com.chingkai56.findhouse.ui.HouseListActivity
import timber.log.Timber

/**
 * Created by timhuang on 2021/2/4.
 **/

fun TextView.textSelect(text:String,isSelect:Boolean){
    this.text = text
    val color = if (isSelect){
        ContextCompat.getColor(context, R.color.colorAccent)
    }else{
        ContextCompat.getColor(context, R.color.default_text)
    }
    setTextColor(color)
}


fun NotificationManager.notifyNew(context: Context) {

    Timber.e("notifyNew called")

    val intent = Intent(context, HouseListActivity::class.java)
    val pi = PendingIntent.getActivity(
            context,
            123,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
    )

    //TODO change this after multiple push are needed
    val notificationId = 1234
    val builder = NotificationCompat.Builder(
            context,
            context.getString(R.string.default_notification_channel_id)
    )
            .setColor(ResourcesCompat.getColor(context.resources,R.color.colorAccent,null))
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("有新的物件")
            .setContentText("點擊查看")
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pi)

    notify(notificationId,builder.build())
}