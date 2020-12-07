package com.chingkai56.findhouse.config

import com.chingkai56.findhouse.data.domain.PriceRange
import com.chingkai56.findhouse.recycler.HouseConfig

/**
 * Created by Tim Huang on 2020/11/28.
 */
object ConfigProvider {
    fun allconfigs():List<HouseConfig>{
        return listOf(
                HouseConfig(id = 1,"自動獲取資料"),
                HouseConfig(id = 2,"有新房子通知我"),
                HouseConfig(id = 3,"過濾重複物件")
        )
    }

    const val CUSTOM_PRICE_ID = 8

    fun priceRangeData(): List<PriceRange>{
        return mutableListOf<PriceRange>().apply {
            add(PriceRange(1, "不限", 0, 999999))
            add(PriceRange(2, "5000以下", 0, 5000))
            add(PriceRange(3, "5000-10000元", 5000, 10000))
            add(PriceRange(4, "10000-15000元", 10000, 15000))
            add(PriceRange(5, "15000-20000元", 15000, 20000))
            add(PriceRange(6, "20000-40000元", 20000, 40000))
            add(PriceRange(7, "40000以上", 40000, 999999))
            add(PriceRange(8, "自訂", 0, 0,isCustom = true))
        }
    }
}

