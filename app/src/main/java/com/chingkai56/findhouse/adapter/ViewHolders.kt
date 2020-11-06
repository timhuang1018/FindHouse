package com.chingkai56.findhouse.adapter

import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chingkai56.findhouse.data.domain.HouseUI
import com.chingkai56.findhouse.ui.OnHouseAction
import kotlinx.android.synthetic.main.item_house.view.*

/**
 * Created by timhuang on 2020/10/13.
 **/

class ListItemViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){

    fun bind(item: HouseUI, listener: OnHouseAction){
        with(itemView.iv_cover){
            Glide.with(this).load(item.cover).into(this)
        }
        itemView.tv_title.text = item.title
        itemView.tv_address.text = item.fullAddress
        itemView.tv_subtitle.text = String.format("%s %s坪 %d/%d",item.layout,item.area.toString(),item.floor,item.allFloor)
        itemView.tv_price.text = item.price
        itemView.setOnClickListener {
            val uri = Uri.parse("https://rent.591.com.tw/rent-detail-${item.id}.html")
            val intent = Intent(Intent.ACTION_VIEW,uri)
            listener.seeHouseProfile(intent)
        }
        itemView.iv_not_good.setOnClickListener {
            listener.sealOrNot(item.id)
        }
    }

}