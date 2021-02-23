package com.chingkai56.findhouse.recycler

import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.chingkai56.findhouse.R
import com.chingkai56.findhouse.data.domain.HouseType
import com.chingkai56.findhouse.data.domain.PriceRangeUI
import com.chingkai56.findhouse.helper.AdapterListener
import com.chingkai56.findhouse.helper.Cell
import com.chingkai56.findhouse.helper.RecyclerItem
import com.chingkai56.findhouse.viewmodels.HouseListViewModel
import kotlinx.android.synthetic.main.item_custom_price.view.*
import kotlinx.android.synthetic.main.item_normal_price_option.view.*
import timber.log.Timber

object PriceCommonCell:Cell<RecyclerItem>(){
    override fun belongsTo(item: RecyclerItem?) = item is PriceRangeUI
    override fun type() = R.layout.item_normal_price_option
    override fun holder(parent: ViewGroup, viewType: Int, viewModel: ViewModel?): RecyclerView.ViewHolder {
       return PriceViewHolder(
               parent.viewOf(viewType),
               viewModel
       )
    }
    override fun bind(holder: RecyclerView.ViewHolder, item: RecyclerItem?, listener: AdapterListener?) {
        if (holder is PriceViewHolder && item is PriceRangeUI){
            holder.bind(item)
        }
    }
}

object PriceCustomCell:Cell<RecyclerItem>(){
    override fun belongsTo(item: RecyclerItem?) = item is PriceRangeUI && item.isCustom
    override fun type() = R.layout.item_custom_price
    override fun holder(parent: ViewGroup, viewType: Int, viewModel: ViewModel?): RecyclerView.ViewHolder {
        return PriceCustomViewHolder(
                parent.viewOf(viewType),
                viewModel
        )
    }
    override fun bind(holder: RecyclerView.ViewHolder, item: RecyclerItem?, listener: AdapterListener?) {
        if (holder is PriceCustomViewHolder && item is PriceRangeUI){
            holder.bind(item)
        }
    }
}

class PriceViewHolder(itemView: View,val viewModel: ViewModel?):RecyclerView.ViewHolder(itemView){

    init {
        if (viewModel is HouseListViewModel){
            itemView.setOnClickListener {
                viewModel.changePrice(adapterPosition)
            }
        }
    }

    fun bind(item: PriceRangeUI){
        itemView.tv_title.apply {
            text = item.rangeName
            val color = if (item.isSelect){
                ContextCompat.getColor(context,R.color.colorAccent)
            }else{
                ContextCompat.getColor(context,R.color.default_text)
            }
            setTextColor(color)
        }
    }
}

class PriceCustomViewHolder(itemView: View,val viewModel: ViewModel?):RecyclerView.ViewHolder(itemView){

    fun bind(item:PriceRangeUI){

        if (item.isSelect){
            itemView.et_price_min.setText(item.min.toString())
            itemView.et_price_max.setText(item.max.toString())
        }
        //setting click listener here because data modification included
        if (viewModel is HouseListViewModel){
            itemView.bt_confirm.setOnClickListener {
                item.min = itemView.et_price_min.text.toString().toInt()
                item.max = itemView.et_price_max.text.toString().toInt()
                viewModel.changePrice(adapterPosition)
            }
        }
    }
}

object PriceDiff:DiffUtil.ItemCallback<PriceRangeUI>(){
    override fun areItemsTheSame(oldItem: PriceRangeUI, newItem: PriceRangeUI): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: PriceRangeUI, newItem: PriceRangeUI): Boolean {
        return oldItem.rangeName == newItem.rangeName
    }

}


class HouseTypeViewHolder(itemView: View,val viewModel: ViewModel?):RecyclerView.ViewHolder(itemView){

    init {
        if (viewModel is HouseListViewModel){
            itemView.setOnClickListener {
                viewModel.changeType(adapterPosition)
            }
        }
    }

    fun bind(item:HouseType){
        itemView.tv_title.apply {
            text = item.name
            val color = if (item.isSelect){
                ContextCompat.getColor(context,R.color.colorAccent)
            }else{
                ContextCompat.getColor(context,R.color.default_text)
            }
            setTextColor(color)
        }
    }
}

object HouseTypeCell: Cell<RecyclerItem>() {
    override fun belongsTo(item: RecyclerItem?) = item is HouseType
    override fun type() = R.layout.item_house_type

    override fun holder(parent: ViewGroup, viewType: Int, viewModel: ViewModel?): RecyclerView.ViewHolder {
        return HouseTypeViewHolder(
                parent.viewOf(viewType),
                viewModel
        )
    }

    override fun bind(holder: RecyclerView.ViewHolder, item: RecyclerItem?, listener: AdapterListener?) {
        if (holder is HouseTypeViewHolder && item is HouseType){
            holder.bind(item)
        }
    }

}



