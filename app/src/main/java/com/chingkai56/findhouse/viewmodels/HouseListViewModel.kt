package com.chingkai56.findhouse.viewmodels

import androidx.lifecycle.*
import com.chingkai56.findhouse.config.ConfigProvider
import com.chingkai56.findhouse.data.domain.HouseUI
import com.chingkai56.findhouse.data.repository.HouseRepository
import com.chingkai56.findhouse.helper.OptionDisplayState
import com.chingkai56.findhouse.helper.RecyclerItem
import kotlinx.coroutines.launch

/**
 * Created by timhuang on 2020/10/14.
 **/

class HouseListViewModel(private val repository:HouseRepository):ViewModel() {


    val isRefreshing = MutableLiveData<Boolean>()

    private val _state = MutableLiveData<OptionDisplayState>(OptionDisplayState.NO_DISPLAY)
    val state :LiveData<OptionDisplayState>
    get() = _state

    //TODO solve put same data repetitively will cause view blinking
    val options :LiveData<List<RecyclerItem>> = state.map {
        when(it){
            OptionDisplayState.PRICE->{
                ConfigProvider.priceRangeData()
            }
            else->{
                listOf()
            }
        }
    }

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

    fun setOptionState(state:OptionDisplayState) {
        //if state is shown, this time will make it not display
        if (_state.value==state){
            _state.value = OptionDisplayState.NO_DISPLAY
            return
        }
        _state.value = state
    }
}

class HouseListViewModelFactory(private val repository:HouseRepository):ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HouseListViewModel(repository) as T
    }
}