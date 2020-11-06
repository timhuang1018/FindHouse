package com.chingkai56.findhouse.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chingkai56.findhouse.data.domain.HouseUI
import com.chingkai56.findhouse.data.repository.HouseRepository
import kotlinx.coroutines.launch

/**
 * Created by timhuang on 2020/10/14.
 **/

class HouseListViewModel:ViewModel() {

    private val repository  = HouseRepository()
    val isRefreshing = MutableLiveData<Boolean>()

    init {
        refresh()
    }


    fun getHouses(): LiveData<List<HouseUI>> {
        return repository.getHouses()
    }

    fun sealHouse(id: Int) {
        repository.sealOrNot(id,toSeal = true)
    }

    fun refresh() {
        viewModelScope.launch {
            repository.fetch()
            isRefreshing.value = false
        }
    }
}