package com.chingkai56.findhouse.data.domain

import com.chingkai56.findhouse.config.HouseKeyWord
import timber.log.Timber

data class OptionStorage(
        val kind:Int,
        val shape:Set<String>?,
        val regionId:Int,
        val sectionId:Set<String>?,
        val areaMin:Int=0,
        val areaMax:Int= Int.MAX_VALUE,
        val patternMore:Set<String>?,
        val priceMin:Int=0,
        val priceMax:Int= Int.MAX_VALUE,
        val options:Set<String>?,
        val priceIndex: Int,
        val typeIndex:Int
){
    fun asQueryParams():Map<String,String>{
        Timber.e("data:$this")
        val params =  hashMapOf(
//                "type" to "1",
                "searchtype" to "1",
                HouseKeyWord.Kind to kind.toString(),
                HouseKeyWord.RegionId to regionId.toString(),
                HouseKeyWord.Area to "$areaMin,$areaMax",
                HouseKeyWord.Rentprice to "$priceMin,$priceMax",
                "hasimg" to "1",
                "not_cover" to "1")
        if (sectionId!=null){
            params[HouseKeyWord.SectionId] = sectionId.joinToString(separator = ",")
        }
        if (shape!=null){
            params[HouseKeyWord.Shape] = shape.joinToString(separator = ",")
        }
        if (patternMore!=null){
            params[HouseKeyWord.PatternMore] = patternMore.joinToString(separator = ",")
        }
        if (options!=null){
            params[HouseKeyWord.Option] = options.joinToString(separator = ",")
        }
        Timber.e("params:$params")
        return params
    }
}
