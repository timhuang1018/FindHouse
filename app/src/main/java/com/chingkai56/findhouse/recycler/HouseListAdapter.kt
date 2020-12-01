package com.chingkai56.findhouse.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chingkai56.findhouse.R
import com.chingkai56.findhouse.data.domain.HouseUI
import com.chingkai56.findhouse.data.domain.ListItem
import com.chingkai56.findhouse.ui.OnHouseAction

/**
 * Created by timhuang on 2020/10/13.
 **/

class HouseListAdapter(private val listener:OnHouseAction):ListAdapter<ListItem,RecyclerView.ViewHolder>(ListDiff) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ListItemViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_house,parent,false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        if (item is HouseUI && holder is ListItemViewHolder){
            holder.bind(item,listener)
        }
    }
}

object ListDiff :DiffUtil.ItemCallback<ListItem>(){
    override fun areItemsTheSame(oldItem: ListItem, newItem: ListItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ListItem, newItem: ListItem): Boolean {
        return oldItem==newItem
    }

}