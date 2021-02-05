package com.chingkai56.findhouse.data.domain

import com.chingkai56.findhouse.data.SelectQuery
import com.chingkai56.findhouse.helper.RecyclerItem

/**
 * Created by timhuang on 2021/2/4.
 **/

data class PriceRangeUI(
        override val id:Int,
        val rangeName:String,
        var min:Int,
        var max:Int,
        val isCustom:Boolean =false,
        override val selectPosition: Int,
        override var isSelect: Boolean = false
): RecyclerItem, SelectQuery


data class HouseType(
        override val id: Int,
        val type :Int,
        val name:String,
        override val selectPosition: Int,
        override var isSelect: Boolean = false
        ): RecyclerItem, SelectQuery
