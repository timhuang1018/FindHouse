package com.chingkai56.findhouse.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.chingkai56.findhouse.adapter.HouseListAdapter
import com.chingkai56.findhouse.databinding.ActivityHouseListBinding
import com.chingkai56.findhouse.viewmodels.HouseListViewModel
import timber.log.Timber

class HouseListActivity : AppCompatActivity(),OnHouseAction {

    private lateinit var binding :ActivityHouseListBinding
    private val adapter = HouseListAdapter(this)
    private val viewModel = HouseListViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHouseListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.verticalList.layoutManager = LinearLayoutManager(this)
        binding.verticalList.adapter = adapter

        viewModel.getHouses().observe(this, Observer {
            adapter.submitList(it)
        })
    }

    override fun seeHouseProfile(intent: Intent) {
        val activities = packageManager?.queryIntentActivities(intent,0) ?: listOf()
        if (activities.isNotEmpty()){
                startActivity(intent)
        }else{
            Timber.e("no browser")
//            toast("尚未安裝line")
        }
    }

    override fun sealHouse(id: Int) {
        viewModel.sealHouse(id)
    }
}

interface OnHouseAction{
    fun seeHouseProfile(intent:Intent)
    fun sealHouse(id: Int)
}