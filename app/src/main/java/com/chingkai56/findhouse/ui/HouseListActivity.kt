package com.chingkai56.findhouse.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.chingkai56.findhouse.R
import com.chingkai56.findhouse.adapter.HouseListAdapter
import com.chingkai56.findhouse.databinding.ActivityHouseListBinding
import com.chingkai56.findhouse.viewmodels.HouseListViewModel

class HouseListActivity : BaseActivity() {

    private lateinit var binding :ActivityHouseListBinding
    private val adapter = HouseListAdapter(this)
    private val viewModel = HouseListViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHouseListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.verticalList.layoutManager = LinearLayoutManager(this)
        binding.verticalList.adapter = adapter

        viewModel.getHouses().observe(this, Observer {
            adapter.submitList(it)
        })

        binding.refreshLayout.setOnRefreshListener {
            viewModel.refresh()
        }

        viewModel.isRefreshing.observe(this,{
            binding.refreshLayout.isRefreshing = it
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.basic_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId==R.id.setting){
            visitSetting()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun visitSetting() {
        val intent = Intent(this,SettingActivity::class.java)
        startActivity(intent)
    }

    override fun sealOrNot(id: Int) {
        viewModel.sealHouse(id)
    }
}

interface OnHouseAction{
    fun seeHouseProfile(intent:Intent)
    fun sealOrNot(id: Int)
}