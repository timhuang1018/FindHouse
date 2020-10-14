package com.chingkai56.findhouse.data.repository

import androidx.lifecycle.LiveData
import com.chingkai56.findhouse.config.SettingApplication
import com.chingkai56.findhouse.data.domain.HouseUI
import com.chingkai56.findhouse.data.source.LocalDataSource
import com.chingkai56.findhouse.fetchData
import timber.log.Timber

/**
 * Created by timhuang on 2020/10/14.
 **/

class HouseRepository {
    private val localDataSource = LocalDataSource(SettingApplication.getDb())

    suspend fun fetch(){
        try {
            var firstRow = 0
            while (firstRow%30==0){
                val result = fetchData(firstRow).data.data
                localDataSource.insertItems(result)
                firstRow += result.size
            }

        }catch (e:Exception){
            Timber.e(e)
        }
    }

    fun getHouses(): LiveData<List<HouseUI>>{
        return localDataSource.getHouses()
    }

    fun sealHouse(id: Int) {
        localDataSource.sealHouse(id)
    }
}