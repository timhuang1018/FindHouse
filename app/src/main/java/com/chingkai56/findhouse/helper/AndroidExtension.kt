package com.chingkai56.findhouse.helper

import android.widget.TextView
import androidx.core.content.ContextCompat
import com.chingkai56.findhouse.R

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