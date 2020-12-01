package com.chingkai56.findhouse.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.chingkai56.findhouse.data.domain.HouseUI
import com.chingkai56.findhouse.data.repository.HouseRepository
import com.chingkai56.findhouse.recycler.HouseConfig

/**
 * Created by timhuang on 2020/11/5.
 **/

class SettingViewModel {
    private val repository  = HouseRepository()

    fun getSealedHouses(): LiveData<List<HouseUI>>{
        return repository.getSealedHouses()
    }



    fun unSealHouse(id: Int) {
        repository.sealOrNot(id,toSeal = false)
    }

    fun configClick(position: Int) {
        //TODO respond to the correct config change
    }

    fun getAllConfigs(): LiveData<List<HouseConfig>> {
        return liveData<List<HouseConfig>> {
            emit(repository.getAllConfigs())
        }
    }
}