package com.chingkai56.findhouse.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.commit
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.chingkai56.findhouse.recycler.HouseListAdapter
import com.chingkai56.findhouse.databinding.ActivitySettingBinding
import com.chingkai56.findhouse.di.DependencyProvider
import com.chingkai56.findhouse.helper.ConfigClickListener
import com.chingkai56.findhouse.recycler.ConfigSwitchAdapter
import com.chingkai56.findhouse.viewmodels.SettingViewModel
import timber.log.Timber

class SettingActivity : BaseActivity(),ConfigClickListener {

    private val adapter = HouseListAdapter(this)
    private val configAdapter = ConfigSwitchAdapter(this)
    private lateinit var binding :ActivitySettingBinding
    private val viewModel : SettingViewModel by viewModels {
        DependencyProvider.provideSettingViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI(savedInstanceState)

        viewModel.getSealedHouses().observe(this, Observer {
            adapter.submitList(it)
        })



//        viewModel.getAllConfigs().observe(this, Observer {
//            configAdapter.submitList(it)
//        })
//
//        binding.switchList.apply {
//            adapter = configAdapter
//            layoutManager = LinearLayoutManager(this@SettingActivity)
//        }
    }

    private fun setupUI(savedInstanceState: Bundle?) {
        supportActionBar?.setDisplayShowTitleEnabled(false)

//        if (savedInstanceState==null){
//            supportFragmentManager.commit {
//                add()
//            }
//        }

        binding.verticalList.layoutManager = LinearLayoutManager(this)
        binding.verticalList.adapter = adapter

    }

    override fun sealOrNot(id: Int) {
        viewModel.unSealHouse(id)
    }

    override fun clickItem(position: Int) {
        Timber.e("position :$position")
        Toast.makeText(this, "got click", Toast.LENGTH_SHORT).show()
        viewModel.configClick(position)
    }
}