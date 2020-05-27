package com.chingkai56.findhouse

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_price_range.view.*

class PriceRangeAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    var items = mutableListOf<PriceRange>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PriceViewHolder(
                LayoutInflater
                        .from(parent.context)
                        .inflate(R.layout.item_price_range,parent,false)
        )
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        if (holder is PriceViewHolder){
            holder.bind(item)
        }
    }

    fun addData(items:List<PriceRange>){
        this.items.addAll(items)
        notifyDataSetChanged()
    }


    inner class PriceViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        fun bind(item:PriceRange){
            itemView.text_price_range.text = item.rangeName
        }
    }
}


data class PriceRange(val id:Int, val rangeName:String, val min:Int, val max:Int)



fun priceRangeData(): List<PriceRange>{
    return mutableListOf<PriceRange>().apply {
        PriceRange(1,"不限",0,999999)
        PriceRange(2,"5000以下",0,5000)
        PriceRange(3,"5000-10000元",5000,10000)
        PriceRange(4,"10000-15000元",10000,15000)
        PriceRange(5,"15000-20000元",15000,20000)
        PriceRange(6,"20000-40000元",20000,40000)
        PriceRange(7,"40000以上",40000,999999)
    }
}