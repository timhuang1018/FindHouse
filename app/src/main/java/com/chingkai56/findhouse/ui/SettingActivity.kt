package com.chingkai56.findhouse.ui

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.chingkai56.findhouse.recycler.HouseListAdapter
import com.chingkai56.findhouse.databinding.ActivitySettingBinding
import com.chingkai56.findhouse.di.DependencyProvider
import com.chingkai56.findhouse.helper.ConfigClickListener
import com.chingkai56.findhouse.recycler.ConfigSwitchAdapter
import com.chingkai56.findhouse.viewmodels.SettingViewModel
import timber.log.Timber

class SettingActivity : BaseActivity(),ConfigClickListener {

    private val sealsAdapter = HouseListAdapter(this)
    private val searchAdapter = HouseListAdapter(this)

    private val configAdapter = ConfigSwitchAdapter(this)
    private lateinit var binding :ActivitySettingBinding
    private val viewModel : SettingViewModel by viewModels {
        DependencyProvider.provideSettingViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        setListener()
        bindData()

//        viewModel.getAllConfigs().observe(this, Observer {
//            configAdapter.submitList(it)
//        })
//
//        binding.switchList.apply {
//            adapter = configAdapter
//            layoutManager = LinearLayoutManager(this@SettingActivity)
//        }
    }

    private fun setListener() {
        binding.btnSearchStart.setOnClickListener {
            binding.verticalList.adapter = searchAdapter
            binding.searchBar.visibility = View.VISIBLE
        }

        binding.btnSearchClose.setOnClickListener {
            binding.verticalList.adapter = sealsAdapter
            binding.searchBar.visibility = View.GONE
        }

        binding.etSearchInput.apply {
            setOnEditorActionListener { v, actionId, event ->
                if (actionId== EditorInfo.IME_ACTION_SEND){
                    viewModel.search(text.toString())
                    return@setOnEditorActionListener true
                }
                false
            }
        }
    }

    private fun bindData() {
        viewModel.getSealedHouses().observe(this, {
            sealsAdapter.submitList(it)
        })

        viewModel.searchResult.observe(this,{
            Timber.e("list size:${it.size}")
            searchAdapter.submitList(it)
        })
    }

    private fun setupUI() {
        supportActionBar?.setDisplayShowTitleEnabled(false)

        binding.verticalList.layoutManager = LinearLayoutManager(this)
        binding.verticalList.adapter = sealsAdapter

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