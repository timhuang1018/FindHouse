package com.chingkai56.findhouse.ui

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.chingkai56.findhouse.adapter.HouseListAdapter
import com.chingkai56.findhouse.databinding.ActivitySettingBinding
import com.chingkai56.findhouse.viewmodels.SettingViewModel

class SettingActivity : BaseActivity() {

    private val adapter = HouseListAdapter(this)
    private lateinit var binding :ActivitySettingBinding
    private val viewModel = SettingViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        binding.verticalList.layoutManager = LinearLayoutManager(this)
        binding.verticalList.adapter = adapter

        viewModel.getSealedHouses().observe(this, Observer {
            adapter.submitList(it)
        })
    }

    override fun sealOrNot(id: Int) {
        viewModel.unSealHouse(id)
    }
}