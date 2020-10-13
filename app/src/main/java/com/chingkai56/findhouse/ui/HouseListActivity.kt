package com.chingkai56.findhouse.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.chingkai56.findhouse.adapter.HouseListAdapter
import com.chingkai56.findhouse.databinding.ActivityHouseListBinding

class HouseListActivity : AppCompatActivity() {

    private lateinit var binding :ActivityHouseListBinding
    private val adapter = HouseListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHouseListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.verticalList.layoutManager = LinearLayoutManager(this)
        binding.verticalList.adapter = adapter
    }
}