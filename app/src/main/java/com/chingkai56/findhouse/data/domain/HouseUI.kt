package com.chingkai56.findhouse.data.domain


/**
 * Created by timhuang on 2020/10/13.
 **/

data class HouseUI(
        override val id:Int,
        val userId: Int,
        val type:Int,
        val kind:Int,
        val postId:Int,
        val regionId:Int?,
        val regionName:String?,
        val sectionName:String?,
        val sectionId: Int?,
        val streetId:Int?,
        val streetName:String?,
        val alleyName:String?,
        val caseName:String?,
        val caseId:String?,
        val layout:String?,
        val area:Double,
        val room:Int,
        val floor:Int,
        val allFloor:Int,
        val updateTime:String?,
        val condition:String,
        val cover:String,
        val refreshTime:String?,
        val closed:Int,
        val kindName:String?,
        val iconClass:String?,
        val fullAddress:String,
        val shape:Int,
        val title:String,
        val price:String
):ListItem