package com.chingkai56.findhouse.data.repository

import androidx.lifecycle.LiveData
import com.chingkai56.findhouse.config.BaseApplication
import com.chingkai56.findhouse.config.ConfigProvider
import com.chingkai56.findhouse.data.domain.HouseUI
import com.chingkai56.findhouse.data.source.LocalDataSource
import com.chingkai56.findhouse.fetchData
import com.chingkai56.findhouse.recycler.HouseConfig
import timber.log.Timber

/**
 * Created by timhuang on 2020/10/14.
 **/

class HouseRepository {
    private val localDataSource = LocalDataSource(BaseApplication.getDb())

    /**
     * if has new data, @return true
     */
    suspend fun fetch(): Boolean {
        Timber.d("fetch called")
        var shouldNotify = false
        try {
            var firstRow = 0
            while (firstRow%30==0){
                val result = fetchData(firstRow).data.data
                val hasNew = localDataSource.insertItems(result)
                if (hasNew){
                    shouldNotify = true
                }
//                else{
//                    break
//                }
                firstRow += result.size
            }
        }catch (e:Exception){
            Timber.e(e)
        }
        return shouldNotify
    }

    fun getHouses(): LiveData<List<HouseUI>>{
        return localDataSource.getHouses()
    }

    fun getSealedHouses(): LiveData<List<HouseUI>> {
        return localDataSource.getSealedHouse()
    }

    fun sealOrNot(id: Int,toSeal:Boolean) {
        localDataSource.sealOrNot(id,toSeal)
    }

    fun getAllConfigs(): List<HouseConfig> {
        return ConfigProvider.allconfigs()
    }
}