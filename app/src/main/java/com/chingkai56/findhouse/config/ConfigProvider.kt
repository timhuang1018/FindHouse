package com.chingkai56.findhouse.config

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
}