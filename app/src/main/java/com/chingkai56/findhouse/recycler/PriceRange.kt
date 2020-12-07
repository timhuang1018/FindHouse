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

class PriceRangeAdapter : ListAdapter<PriceRange,RecyclerView.ViewHolder>(PriceDiff){

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).id==ConfigProvider.CUSTOM_PRICE_ID) TYPE_CUSTOM else TYPE_NORMAL
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
         if (viewType== TYPE_CUSTOM){
             return PriceCustomViewHolder(
                     LayoutInflater
                             .from(parent.context)
                             .inflate(R.layout.item_custom_price,parent,false)
             )
         }else {
             return PriceViewHolder(
                     LayoutInflater
                             .from(parent.context)
                             .inflate(R.layout.item_price_range,parent,false)
             )
         }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        Log.d("adapter","item :$item")
        if (holder is PriceViewHolder){
            holder.bind(item)
        }
    }

    companion object{
        const val TYPE_NORMAL = 0
        const val TYPE_CUSTOM = 1
    }

}

object PriceCommonCell:Cell<RecyclerItem>(){
    override fun belongsTo(item: RecyclerItem?) = item is PriceRange
    override fun type() = R.layout.item_price_range
    override fun holder(parent: ViewGroup, viewType: Int, viewModel: ViewModel?): RecyclerView.ViewHolder {
       return PriceViewHolder(
               parent.viewOf(viewType)
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
                parent.viewOf(viewType)
        )
    }
    override fun bind(holder: RecyclerView.ViewHolder, item: RecyclerItem?, listener: AdapterListener?) {
        if (holder is PriceViewHolder && item is PriceRange){
            holder.bind(item)
        }
    }
}

class PriceViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
    fun bind(item: PriceRange){
        itemView.textview_price_range.text = item.rangeName
    }
}

class PriceCustomViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
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



