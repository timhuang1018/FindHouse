package com.chingkai56.findhouse.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chingkai56.findhouse.config.BaseApplication
import com.chingkai56.findhouse.config.ConfigProvider
import com.chingkai56.findhouse.data.domain.HouseUI
import com.chingkai56.findhouse.data.domain.OptionStorage
import com.chingkai56.findhouse.data.domain.PriceRangeUI
import com.chingkai56.findhouse.data.domain.QueryPreview
import com.chingkai56.findhouse.data.source.LocalDataSource
import com.chingkai56.findhouse.data.source.SharePrefStorage
import com.chingkai56.findhouse.fetchData
import com.chingkai56.findhouse.helper.RecyclerItem
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

    private val _searchOptions = MutableLiveData<OptionStorage>()
    val searchOptions :LiveData<OptionStorage>
    get() = _searchOptions

    init {
        updateOptions()
    }

    /**
     * if has new data, @return true
     */
    suspend fun fetch(): Boolean {
        var shouldNotify = false
        val options = _searchOptions.value ?: return false
        Timber.d("fetch called")
        try {
            var firstRow = 0
            //TODO if options change while fetching, restart fetching
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

    fun getHouses(optionStorage: OptionStorage): LiveData<List<HouseUI>>{
        return localDataSource.getHouses(optionStorage)
    }

    fun getSealedHouses(): LiveData<List<HouseUI>> {
        return localDataSource.getSealedHouse()
    }

    fun sealOrNot(id: Int,toSeal:Boolean) {
        localDataSource.sealOrNot(id,toSeal)
    }

    fun getAllConfigs(): List<HouseConfig> {
        return ConfigProvider().allconfigs()
    }

    fun getPriceOptions(): List<RecyclerItem> {
        return ConfigProvider().priceRangeData(sharePref)
    }

    fun putPriceChange(item: PriceRangeUI) {
        Timber.e("putPriceChange called")
        sharePref.putPriceRange(item)
        //refresh option config
        updateOptions()
    }

    private fun updateOptions() {
        _searchOptions.value = sharePref.getQueryCondition()
    }

    fun getPricePreview(priceIndex: Int): QueryPreview {
        return ConfigProvider().getPriceTitle(priceIndex,sharePref)
    }
}