package com.chingkai56.findhouse.recycler

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chingkai56.findhouse.R
import com.chingkai56.findhouse.config.ConfigProvider
import com.chingkai56.findhouse.data.domain.PriceRange
import com.chingkai56.findhouse.helper.AdapterListener
import com.chingkai56.findhouse.helper.Cell
import com.chingkai56.findhouse.helper.RecyclerItem
import kotlinx.android.synthetic.main.item_price_range.view.*

object PriceCommonCell:Cell<RecyclerItem>(){
    override fun belongsTo(item: RecyclerItem?) = item is PriceRange
    override fun type() = R.layout.item_price_range
    override fun holder(parent: ViewGroup, viewType: Int, viewModel: ViewModel?): RecyclerView.ViewHolder {
       return PriceViewHolder(
               parent.viewOf(viewType),
               viewModel
       )
    }
    override fun bind(holder: RecyclerView.ViewHolder, item: RecyclerItem?, listener: AdapterListener?) {
        if (holder is PriceViewHolder && item is PriceRange){
            holder.bind(item)
        }
    }
}

object PriceCustomCell:Cell<RecyclerItem>(){
    override fun belongsTo(item: RecyclerItem?) = item is PriceRange && item.isCustom
    override fun type() = R.layout.item_custom_price
    override fun holder(parent: ViewGroup, viewType: Int, viewModel: ViewModel?): RecyclerView.ViewHolder {
        return PriceCustomViewHolder(
                parent.viewOf(viewType),
                viewModel
        )
    }
    override fun bind(holder: RecyclerView.ViewHolder, item: RecyclerItem?, listener: AdapterListener?) {
        if (holder is PriceViewHolder && item is PriceRange){
            holder.bind(item)
        }
    }
}

class PriceViewHolder(itemView: View,val viewModel: ViewModel?):RecyclerView.ViewHolder(itemView){
    fun bind(item: PriceRange){
        itemView.textview_price_range.text = item.rangeName
    }
}

class PriceCustomViewHolder(itemView: View,val viewModel: ViewModel?):RecyclerView.ViewHolder(itemView){
    fun bind(item:PriceRange){

    }
}

object PriceDiff:DiffUtil.ItemCallback<PriceRange>(){
    override fun areItemsTheSame(oldItem: PriceRange, newItem: PriceRange): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: PriceRange, newItem: PriceRange): Boolean {
        return oldItem.rangeName == newItem.rangeName
    }

}



