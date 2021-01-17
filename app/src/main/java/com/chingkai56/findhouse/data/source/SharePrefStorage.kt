package com.chingkai56.findhouse.data.source

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.chingkai56.findhouse.config.HouseKeyWord
import com.chingkai56.findhouse.data.domain.OptionStorage
import com.chingkai56.findhouse.recycler.HouseConfig

/**
 * Created by TimHuang on 2020/12/9.
 */
class SharePrefStorage(context:Context) {
    private val sharePref:SharedPreferences = context.getSharedPreferences(HouseKeyWord.ApplicationName,Activity.MODE_PRIVATE)
    fun getQueryCondition(): OptionStorage {
        return sharePref.run {
            OptionStorage(
                    kind = getInt(HouseKeyWord.Kind,0),
                    shape = getStringSet(HouseKeyWord.Shape, null),
                    //TODO for now just assign 1 (台北市 )
                    regionId = getInt(HouseKeyWord.RegionId,1),
                    areaMin = getInt(HouseKeyWord.AreaMin,0),
                    areaMax = getInt(HouseKeyWord.AreaMax,Int.MAX_VALUE),
                    patternMore = getStringSet(HouseKeyWord.PatternMore,null),
                    priceMin = getInt(HouseKeyWord.PriceMin,0),
                    priceMax = getInt(HouseKeyWord.PriceMax,Int.MAX_VALUE),
                    options = getStringSet(HouseKeyWord.Option,null)

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



}