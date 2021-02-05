package com.chingkai56.findhouse.data.source

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.chingkai56.findhouse.config.HouseKeyWord
import com.chingkai56.findhouse.data.domain.HouseType
import com.chingkai56.findhouse.data.domain.OptionStorage
import com.chingkai56.findhouse.data.domain.PriceRangeUI

/**
 * Created by TimHuang on 2020/12/9.
 */
class SharePrefStorage(context:Context) {
    private val sharePref:SharedPreferences = context.getSharedPreferences(HouseKeyWord.ApplicationName,Activity.MODE_PRIVATE)
    fun getQueryCondition(): OptionStorage {
        return sharePref.run {
            OptionStorage(
                    kind = getInt(HouseKeyWord.Type,0),
                    shape = getStringSet(HouseKeyWord.Shape, null),
                    //TODO for now just assign 1 (台北市 )
                    regionId = getInt(HouseKeyWord.RegionId,1),
                    areaMin = getInt(HouseKeyWord.AreaMin,0),
                    areaMax = getInt(HouseKeyWord.AreaMax,Int.MAX_VALUE),
                    patternMore = getStringSet(HouseKeyWord.PatternMore,null),
                    priceMin = getInt(HouseKeyWord.PriceMin,0),
                    priceMax = getInt(HouseKeyWord.PriceMax,50000),
                    //these options is for query param in 591
                    options = getStringSet(HouseKeyWord.Option,null),
                    priceIndex = getInt(HouseKeyWord.PriceSelectIndex,-1),
                    typeIndex = getInt(HouseKeyWord.Type,-1)
            )
        }
    }

    fun addStringSet(key:String,value:String){
        val set = sharePref.getStringSet(key,null)
        if (set==null){
            sharePref.edit {
                putStringSet(key, setOf(value))
            }
        }else{
            set.add(value)
            sharePref.edit {
                putStringSet(key,set)
            }
        }
    }

    fun removeStringSet(key:String,value:String){
        val set = sharePref.getStringSet(key,null)
        if (set!=null && set.contains(key)){
            set.remove(key)
            sharePref.edit {
                putStringSet(key, set)
            }
        }
    }

    fun removeAll(key: String){
        sharePref.edit {
            remove(key)
        }
    }

    fun getPriceSelectIndex(): Int {
        //-1 means no selected index
        return sharePref.getInt(HouseKeyWord.PriceSelectIndex,-1)
    }

    fun putPriceRange(item: PriceRangeUI) {
        sharePref.edit {
            //only id 1 is special (will clear up select position
            if (item.id==1){
                remove(HouseKeyWord.PriceSelectIndex)
            }else{
                putInt(HouseKeyWord.PriceSelectIndex,item.selectPosition)
            }
            putInt(HouseKeyWord.PriceMax,item.max)
            putInt(HouseKeyWord.PriceMin,item.min)
        }
    }

    fun getPriceMin(): Int {
        return sharePref.getInt(HouseKeyWord.PriceMin,0)
    }

    fun getPriceMax(): Int {
        return sharePref.getInt(HouseKeyWord.PriceMax,0)
    }

    fun getTypeSelectIndex(): Int {
        return sharePref.getInt(HouseKeyWord.TypeSelectIndex,0)
    }

    fun putHouseType(item: HouseType) {
        sharePref.edit {
            if (item.id==1){
                remove(HouseKeyWord.TypeSelectIndex)
            }else{
                putInt(HouseKeyWord.TypeSelectIndex,item.selectPosition)
            }
            putInt(HouseKeyWord.Type,item.type)
        }
    }


}