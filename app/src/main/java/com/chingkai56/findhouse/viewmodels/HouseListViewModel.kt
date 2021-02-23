package com.chingkai56.findhouse.viewmodels

import androidx.lifecycle.*
import com.chingkai56.findhouse.data.domain.HouseType
import com.chingkai56.findhouse.data.domain.OptionStorage
import com.chingkai56.findhouse.data.domain.PriceRangeUI
import com.chingkai56.findhouse.data.repository.HouseRepository
import com.chingkai56.findhouse.helper.EventWrapper
import com.chingkai56.findhouse.helper.OptionDisplayState
import com.chingkai56.findhouse.helper.RecyclerItem
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import timber.log.Timber

/**
 * Created by timhuang on 2020/10/14.
 **/

class HouseListViewModel(private val repository:HouseRepository):ViewModel() {

//    val mutext = Mutex()
    val isRefreshing = MutableLiveData<Boolean>()

    private val _state = MutableLiveData<OptionDisplayState>(OptionDisplayState.NO_DISPLAY)
    val state :LiveData<OptionDisplayState>
    get() = _state

    private val _notifiyNew = MutableLiveData<EventWrapper<Boolean>>()
    val notifyNew :LiveData<EventWrapper<Boolean>>
    get() = _notifiyNew


    private val _searchOptions = MutableLiveData<OptionStorage>()
    val searchOptions :LiveData<OptionStorage>
        get() = _searchOptions

    //TODO remove below two variable , bad practice to observe in viewModel
    private val fetch = searchOptions.map {
        refresh()
    }
    private val doNothingObserver = Observer<Unit> { }

    val houses = searchOptions.switchMap {
        repository.getHouses(it)
    }

    val pricePreview = searchOptions.map {
        repository.getPricePreview(it.priceIndex)
    }

    val typePreview = searchOptions.map {
        repository.getTypePreview(it.typeIndex)
    }

    //TODO solve put same data repetitively will cause view blinking, since state triger each time
    val listItems :LiveData<List<RecyclerItem>> = state.map {
        Timber.e("state:$it")
        when(it){
            OptionDisplayState.PRICE->{
                repository.getPriceOptions()
            }
            OptionDisplayState.TYPE->{
                repository.getTypeOptions()
            }
            else->{
                listOf()
            }
        }
    }

    init {
        fetch.observeForever(doNothingObserver)
        updateOptions()
    }

//    fun getHouses(): LiveData<List<HouseUI>> {
//        return
//    }

    fun sealHouse(id: Int) {
        repository.sealOrNot(id,toSeal = true)
    }

    fun refresh() {
        viewModelScope.launch {
//            mutext.withLock {
                val shouldNotify = repository.fetch()
                _notifiyNew.value = EventWrapper(shouldNotify)
                isRefreshing.value = false
//            }
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

    fun changePrice(itemIdex: Int) {
        listItems.value?.takeIf { it.size>itemIdex }?.let { list->
            val item = list[itemIdex]
            if(item is PriceRangeUI){
                repository.putPriceChange(item)
                updateOptions()
            }
        }
        _state.value = OptionDisplayState.NO_DISPLAY
    }

    override fun onCleared() {
        super.onCleared()
        fetch.removeObserver(doNothingObserver)
    }

    fun changeType(position: Int) {
        listItems.value?.takeIf { it.size>position }?.let { list->
            val item = list[position]
            if(item is HouseType){
                repository.putTypeChange(item)
                updateOptions()
            }
        }
        _state.value = OptionDisplayState.NO_DISPLAY
    }


    private fun updateOptions() {
        _searchOptions.value = repository.getOptionStorage()
    }
}

class HouseListViewModelFactory(private val repository:HouseRepository):ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HouseListViewModel(repository) as T
    }
}