package com.chingkai56.findhouse.di

import android.content.Context
import com.chingkai56.findhouse.data.repository.HouseRepository
import com.chingkai56.findhouse.data.source.SharePrefStorage
import com.chingkai56.findhouse.viewmodels.HouseListViewModelFactory
import com.chingkai56.findhouse.viewmodels.SettingViewModelFactory

object DependencyProvider {

    fun provideHouseListViewModelFactory(context: Context): HouseListViewModelFactory {
        return HouseListViewModelFactory(HouseRepository.getInstance(context))
    }

    fun provideSettingViewModelFactory(context: Context):SettingViewModelFactory{
        return SettingViewModelFactory(HouseRepository.getInstance(context))
    }
}