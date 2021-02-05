package com.chingkai56.findhouse.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.view.forEach
import androidx.core.view.isNotEmpty
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.chingkai56.findhouse.R
import com.chingkai56.findhouse.databinding.ActivityHouseListBinding
import com.chingkai56.findhouse.di.DependencyProvider
import com.chingkai56.findhouse.helper.BaseListAdapter
import com.chingkai56.findhouse.helper.OptionDisplayState
import com.chingkai56.findhouse.helper.textSelect
import com.chingkai56.findhouse.recycler.HouseListAdapter
import com.chingkai56.findhouse.recycler.HouseTypeCell
import com.chingkai56.findhouse.recycler.PriceCommonCell
import com.chingkai56.findhouse.recycler.PriceCustomCell
import com.chingkai56.findhouse.viewmodels.HouseListViewModel
import timber.log.Timber


class HouseListActivity : BaseActivity() {

    private lateinit var binding :ActivityHouseListBinding
    private val adapter = HouseListAdapter(this)
    private val viewModel : HouseListViewModel by viewModels{
        DependencyProvider.provideHouseListViewModelFactory(this)
    }
    private lateinit var optionAdapter :BaseListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupUI()
        setListener()
        dataBinding()
    }

    private fun setupUI() {
        binding = ActivityHouseListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.verticalList.layoutManager = LinearLayoutManager(this)
        binding.verticalList.adapter = adapter

        optionAdapter = BaseListAdapter(HouseTypeCell,PriceCustomCell,PriceCommonCell,viewModel = viewModel)

        binding.recyclerOptions.apply {
            adapter = optionAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun dataBinding() {
        viewModel.listItems.observe(this,{
            Timber.e("list :$it")
            optionAdapter.submitList(it)
        })

        viewModel.state.observe(this, Observer { state ->
            when(state){
                OptionDisplayState.NO_DISPLAY->{
                    optionVisible(noOptions = true)
                }
                OptionDisplayState.LOCATION->{
                    optionVisible(binding.tvLocationOption)
                }
                OptionDisplayState.TYPE->{
                    optionVisible(binding.tvTypeOption)
                }

                OptionDisplayState.PRICE->{
                    optionVisible(binding.tvPriceOption)
                }
                OptionDisplayState.MORE->{
                    optionVisible(binding.tvMoreOptions)
                }
            }
        })

        viewModel.houses.observe(this, Observer {
//            Timber.e("houses:$it")
            adapter.submitList(it)
        })

        viewModel.isRefreshing.observe(this,{
            binding.refreshLayout.isRefreshing = it
        })

        viewModel.pricePreview.observe(this,{
            Timber.e("price preview:$it")
            binding.tvPriceOption.textSelect(it.title,it.isSelect)
        })

        viewModel.typePreview.observe(this,{
            binding.tvTypeOption.textSelect(it.title,it.isSelect)
        })
    }

    private fun setListener() {
        binding.locationOptionContainer.setOnClickListener{
            viewModel.setOptionState(OptionDisplayState.LOCATION)
        }
        binding.typeOptionContainer.setOnClickListener{
            viewModel.setOptionState(OptionDisplayState.TYPE)
        }
        binding.priceOptionContainer.setOnClickListener{
            viewModel.setOptionState(OptionDisplayState.PRICE)
        }
        binding.moreOptionContainer.setOnClickListener{
            viewModel.setOptionState(OptionDisplayState.MORE)
        }

        binding.refreshLayout.setOnRefreshListener {
            viewModel.refresh()
        }
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

    private fun optionVisible(view: TextView?=null,noOptions:Boolean=false) {
        binding.recyclerOptions.apply {
            visibility = if (noOptions){
                View.GONE
            }else{
                View.VISIBLE
            }
        }
        //turn every arrow to drop down
        val ll = binding.root.getChildAt(0)
        if (ll is LinearLayout){
            ll.forEach { innerLL->
                if (innerLL is LinearLayout && innerLL.isNotEmpty()){
                    innerLL.getChildAt(0).takeIf {it is TextView}?.let { textview->
                        (textview as TextView).setCompoundDrawablesWithIntrinsicBounds(null,null,ContextCompat.getDrawable(this,R.drawable.ic_arrow_drop_down),null)
                    }
                }
            }
        }
        //make necessary arrow to drop up
        view?.setCompoundDrawablesWithIntrinsicBounds(null,null,ContextCompat.getDrawable(this,R.drawable.ic_arrow_drop_up),null)
    }
}

interface OnHouseAction{
    fun seeHouseProfile(intent:Intent)
    fun sealOrNot(id: Int)
}