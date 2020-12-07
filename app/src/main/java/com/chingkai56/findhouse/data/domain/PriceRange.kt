package com.chingkai56.findhouse.data.domain

import com.chingkai56.findhouse.helper.RecyclerItem

data class PriceRange(override val id:Int, val rangeName:String, val min:Int, val max:Int,val isCustom:Boolean =false):RecyclerItem