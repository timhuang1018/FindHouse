package com.chingkai56.findhouse.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.chingkai56.findhouse.config.BaseApplication
import com.chingkai56.findhouse.config.ConfigProvider
import com.chingkai56.findhouse.data.domain.*
import com.chingkai56.findhouse.data.source.LocalDataSource
import com.chingkai56.findhouse.data.source.SharePrefStorage
import com.chingkai56.findhouse.fetchData
import com.chingkai56.findhouse.helper.RecyclerItem
import com.chingkai56.findhouse.helper.SingletonHolder
import com.chingkai56.findhouse.recycler.HouseConfig
import com.chingkai56.findhouse.work.FetchHousesWork
import timber.log.Timber

/**
 * Created by timhuang on 2020/10/14.
 * Aim for dealing data processing in this layer, call remote to fetch
 * Hold one search option live data to represent all search option , relate ui display count on this,
 * any modification of option should be operated by [HouseRepository]
 **/

class HouseRepository private constructor(
//        private val remoteDataSource:RemoteDataSource,
        context: Context,
        private val sharePref: SharePrefStorage,
        private val localDataSource :LocalDataSource= LocalDataSource(BaseApplication.getDb())) {

    private var methodId = 0

    init {

        startWork(context)


    }

    private fun startWork(context: Context) {
        //user not setting to fetch data at background yet
        if (!sharePref.getStartWork()){
            return
        }
        FetchHousesWork.startWork(context)
    }

    /**
     * if has new data, @return true
     */
    suspend fun fetch(): Boolean {
        var shouldNotify = false
        val options = sharePref.getQueryCondition()
        methodId++
        val id = methodId
        Timber.e("fetch remote data by option:$options")
        try {
            var firstRow = 0
            //TODO if options change while fetching, restart fetching
            while (firstRow%30==0 && id==methodId){
                Timber.d("fetch called row:$firstRow")
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
        //if user not setting notify new, don't send
        if (!sharePref.getNotifyNew()) shouldNotify = false
        return shouldNotify
    }

    fun getHouses(optionStorage: OptionStorage): LiveData<List<HouseUI>>{
        return localDataSource.getHouses(optionStorage)
    }

    fun getSealedHouses(): LiveData<List<HouseUI>> {
        return localDataSource.getSealedHouses()
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
    }


    fun getPricePreview(priceIndex: Int): QueryPreview {
        return ConfigProvider().getPricePreview(priceIndex,sharePref)
    }

    fun getTypeOptions(): List<RecyclerItem> {
        return ConfigProvider().typeData(sharePref)
    }

    fun putTypeChange(item: HouseType) {
        sharePref.putHouseType(item)
    }

    fun getTypePreview(index: Int) :QueryPreview{
        return ConfigProvider().getTypePreview(index)
    }

    fun getOptionStorage(): OptionStorage {
        return sharePref.getQueryCondition()
    }

    /**
     * Add this to become a lazy singleton thanks to [SingletonHolder]
     */
    companion object: SingletonHolder<HouseRepository,Context>({ context -> HouseRepository(context = context,sharePref = SharePrefStorage(context)) })
}