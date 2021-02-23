package com.chingkai56.findhouse.helper

/**
 * Created by timhuang on 2021/2/18.
 **/

open class EventWrapper<out T>(private val content:T){
    var hasBeenHandled =false
        private set

    fun getContentIfNotHandled():T?{
        return if (hasBeenHandled){
            null
        }else{
            hasBeenHandled = true
            content
        }
    }

    fun peekContent():T = content

    override fun toString(): String {
        return "event wrapper has content:${content.toString()}"
    }
}
