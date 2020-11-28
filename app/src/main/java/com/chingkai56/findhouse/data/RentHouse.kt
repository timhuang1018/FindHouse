package com.chingkai56.findhouse.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RentHouse(
        @SerialName("id")
        val houseId:Int,
        @SerialName("user_id")
        val userId: Int,
        val type:Int,
        val kind:Int,
        @SerialName("post_id")
        val postId:Int,
        @SerialName("regionid")
        val regionId:Int?,
        @SerialName("regionname")
        val regionName:String?,
        @SerialName("sectionname")
        val sectionName:String?,
        @SerialName("sectionid")
        val sectionId: Int?,
        @SerialName("streetid")
        val streetId:Int?,
        @SerialName("street_name")
        val streetName:String?,
        @SerialName("alley_name")
        val alleyName:String?,
        @SerialName("cases_name")
        val caseName:String?,
        @SerialName("cases_id")
        val caseId:String?,
        val layout:String?,
        val area:Double,
        val room:Int,
        val floor:Int,
        @SerialName("allfloor")
        val allFloor:Int,
        @SerialName("updatetime")
        val updateTime:String?,
        val condition:String,
        val cover:String,
        @SerialName("refreshtime")
        val refreshTime:String?,
        val closed:Int,
        @SerialName("kind_name_img")
        val kindName:String?,
        @SerialName("icon_class")
        val iconClass:String?,
        @SerialName("fulladdress")
        val fullAddress:String,
        val shape:Int,
        @SerialName("address_img_title")
        val title:String,
        val price:String
)

@Serializable
data class SearchJson(val status:Int,val data:InnerData)

@Serializable
data class InnerData(val data:List<RentHouse>)
