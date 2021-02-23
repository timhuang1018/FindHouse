package com.chingkai56.findhouse.data.domain

import com.chingkai56.findhouse.config.HouseKeyWord


/*
      data("is_new_list","1")
                        "type","1")
                        "kind","1")
                        "shape","2")
                        "searchtype","1")
                        "regionid","1")
                        "area","13,40")
                        "patternMore","1,2")
                        "rentprice","14000,27000")
                        "option","cold")
                        "hasimg","1")
                        "not_cover", "1")
 */
data class OptionStorage(
        val kind:Int,
        val shape:Set<String>?,
        val regionId:Int,
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
        val params =  mapOf(
//                "type" to "1",
                "searchtype" to "1",
                HouseKeyWord.Kind to kind.toString(),
                HouseKeyWord.RegionId to regionId.toString(),
                HouseKeyWord.Area to "$areaMin,$areaMax",
                HouseKeyWord.Rentprice to "$priceMin,$priceMax",
                "hasimg" to "1",
                "not_cover" to "1")
        if (shape!=null){
            HouseKeyWord.Shape to shape.joinToString(separator = ",")
        }
        if (patternMore!=null){
            HouseKeyWord.PatternMore to patternMore.joinToString(separator = ",")
        }
        if (options!=null){
            HouseKeyWord.Option to options.joinToString(separator = ",")
        }
        return params
    }
}
