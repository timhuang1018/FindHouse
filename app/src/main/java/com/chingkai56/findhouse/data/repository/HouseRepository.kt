package com.chingkai56.findhouse.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chingkai56.findhouse.config.BaseApplication
import com.chingkai56.findhouse.config.ConfigProvider
import com.chingkai56.findhouse.data.domain.HouseUI
import com.chingkai56.findhouse.data.domain.OptionStorage
import com.chingkai56.findhouse.data.source.LocalDataSource
import com.chingkai56.findhouse.data.source.RemoteDataSource
import com.chingkai56.findhouse.data.source.SharePrefStorage
import com.chingkai56.findhouse.fetchData
import com.chingkai56.findhouse.recycler.HouseConfig
import timber.log.Timber

/**
 * Created by timhuang on 2020/10/14.
 * Aim for dealing data processing in this layer, call remote to fetch
 * Hold one search option live data to represent all search option , relate ui display count on this,
 * any modification of option should be operated by [HouseRepository]
 **/

class HouseRepository(
//        private val remoteDataSource:RemoteDataSource,
        private val sharePref: SharePrefStorage,
        private val localDataSource :LocalDataSource= LocalDataSource(BaseApplication.getDb())) {

    private val searchOptions = MutableLiveData<OptionStorage>()

    /**
     * if has new data, @return true
     */
    suspend fun fetch(): Boolean {
        Timber.d("fetch called")
        var shouldNotify = false
        val options = searchOptions.value ?: return false
        try {
            var firstRow = 0

            while (firstRow%30==0){
                val result = fetchData(options.asQueryParams(),firstRow).data.data
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