package com.chingkai56.findhouse.config

import com.chingkai56.findhouse.data.domain.HouseType
import com.chingkai56.findhouse.data.domain.PriceRangeUI
import com.chingkai56.findhouse.data.domain.QueryPreview
import com.chingkai56.findhouse.data.source.SharePrefStorage
import com.chingkai56.findhouse.recycler.HouseConfig

/**
 * Created by Tim Huang on 2020/11/28.
 */
class ConfigProvider {





    fun allconfigs():List<HouseConfig>{
        return listOf(
                HouseConfig(id = 1,"自動獲取資料"),
                HouseConfig(id = 2,"有新房子通知我"),
                HouseConfig(id = 3,"過濾重複物件")
        )
    }


    fun priceRangeData(sharePref: SharePrefStorage): List<PriceRangeUI>{
        return providePriceList(sharePref).also {
            it.forEach { it.isSelect = it.selectPosition==sharePref.getPriceSelectIndex() }
        }
    }

    /**
     * ask for what to display on price option display and if it is selected
     */
    fun getPricePreview(priceSelectIndex: Int, sharePref: SharePrefStorage): QueryPreview {
        val item = providePriceList(sharePref).find { it.selectPosition==priceSelectIndex }
        if (item?.selectPosition==CUSTOM_PRICE){
            return QueryPreview(
                    "${item.min}-${item.max}",
                    item!=null
            )
        }
        return QueryPreview(
                item?.rangeName ?: "租金",
                item!=null
        )
    }


    fun providePriceList(sharePref: SharePrefStorage): List<PriceRangeUI> {
        val selectIndex = sharePref.getPriceSelectIndex()
        return listOf<PriceRangeUI>(
                //TODO the max is limited now
                (PriceRangeUI(1, "不限", 0, 50000,selectPosition = 0))
                ,(PriceRangeUI(2, "5000以下", 0, 5000,selectPosition = 1))
                ,(PriceRangeUI(3, "5000-10000元", 5000, 10000,selectPosition = 2))
                ,(PriceRangeUI(4, "10000-15000元", 10000, 15000,selectPosition = 3))
                ,(PriceRangeUI(5, "15000-20000元", 15000, 20000,selectPosition = 4))
                ,(PriceRangeUI(6, "20000-40000元", 20000, 40000,selectPosition = 5))
                ,(PriceRangeUI(7, "40000以上", 40000, 50000,selectPosition = 6))
                ,(PriceRangeUI(8, "自訂", 0, 0,isCustom = true,selectPosition = CUSTOM_PRICE)).also {
                    if (selectIndex==it.selectPosition){
                        it.min = sharePref.getPriceMin()
                        it.max = sharePref.getPriceMax()
                    }
        }
        )
    }

    fun typeData(sharePref: SharePrefStorage): List<HouseType> {
        return provideTypeList().also {
            it.forEach { it.isSelect = it.selectPosition==sharePref.getTypeSelectIndex() }
        }
    }

    private fun provideTypeList(): List<HouseType> {
        return listOf(
                HouseType(id = 1,type = 0,name = "不限",0),
                HouseType(id = 2,type = 1,name = "整層住家",1),
                HouseType(id = 3,type = 2,name = "獨立套房",2),
                HouseType(id = 4,type = 3,name = "分租套房",3),
                HouseType(id = 5,type = 4,name = "雅房",4),
        )
    }

    fun getTypePreview(index: Int): QueryPreview {
        val item = provideTypeList().find { it.selectPosition==index }
        return QueryPreview(
                title = item?.name ?: "類型",
                isSelect = item!=null
        )
    }

    companion object{

        const val CUSTOM_PRICE = 7
    }
}

