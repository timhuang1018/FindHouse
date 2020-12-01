package com.chingkai56.findhouse.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chingkai56.findhouse.databinding.ItemConfigSwitchBinding
import com.chingkai56.findhouse.helper.ConfigClickListener
import timber.log.Timber

/**
 * Created by Tim Huang on 2020/11/28.
 */

class ConfigSwitchAdapter(private val listener: ConfigClickListener):ListAdapter<HouseConfig,ConfigSwitchViewHolder>(Diff){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConfigSwitchViewHolder {
        return ConfigSwitchViewHolder(
                listener,
                ItemConfigSwitchBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }

    override fun onBindViewHolder(holder: ConfigSwitchViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}

data class HouseConfig(val id:Int,val name:String,val isOn:Boolean = false)
object Diff:DiffUtil.ItemCallback<HouseConfig>(){
    override fun areItemsTheSame(oldItem: HouseConfig, newItem: HouseConfig) = oldItem.id==newItem.id
    override fun areContentsTheSame(oldItem: HouseConfig, newItem: HouseConfig) = oldItem.isOn == newItem.isOn

}

class ConfigSwitchViewHolder(val listener:ConfigClickListener, val binding:ItemConfigSwitchBinding):RecyclerView.ViewHolder(binding.root),View.OnClickListener{

    init {
        binding.switchCover.setOnClickListener(this)
    }

    fun bind(item:HouseConfig){
        binding.tvConfigName.text = item.name
        binding.switchButton.isChecked = item.isOn
    }

    override fun onClick(v: View?) {
        listener.clickItem(adapterPosition)
    }
}