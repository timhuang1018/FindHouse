package com.chingkai56.findhouse.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chingkai56.findhouse.config.SettingApplication
import com.chingkai56.findhouse.data.domain.HouseUI
import com.chingkai56.findhouse.data.domain.ListItem
import com.chingkai56.findhouse.data.repository.HouseRepository
import kotlinx.coroutines.launch

/**
 * Created by timhuang on 2020/10/14.
 **/

class HouseListViewModel:ViewModel() {

    private val repository  = HouseRepository()

    private val _data = MutableLiveData<List<ListItem>>()
    val data :LiveData<List<ListItem>>
    get() = _data

    init {
        viewModelScope.launch {
            repository.fetch()
        }
    }


    fun getHouses(): LiveData<List<HouseUI>> {
        return repository.getHouses()
    }

    fun sealHouse(id: Int) {
        repository.sealHouse(id)
    }
}