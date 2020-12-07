package com.chingkai56.findhouse.helper

/**
 * Created by Tim Huang on 2020/12/7.
 */

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber

open class BaseListAdapter (vararg types: Cell<RecyclerItem>, private val listener: AdapterListener?=null)
    : ListAdapter<RecyclerItem, RecyclerView.ViewHolder>(
        BASE_DIFF_CALLBACK
){

    val cellTypes: CellTypes<RecyclerItem> =
            CellTypes(*types)


    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return cellTypes.of(item).type()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return cellTypes.of(viewType).holder(parent, viewType)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        cellTypes.of(item).bind(holder, item, listener)
    }

}

interface RecyclerItem{
    val id:Int
    override fun equals(other:Any?):Boolean
}

interface AdapterListener{
    fun listen(
            clickItem: AdapterClick?,
            reply: Boolean?=null
    )
}

interface AdapterClick

val BASE_DIFF_CALLBACK = object:DiffUtil.ItemCallback<RecyclerItem>(){
    override fun areItemsTheSame(oldItem: RecyclerItem, newItem: RecyclerItem): Boolean {
        Timber.e("areItemsTheSame called")
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: RecyclerItem, newItem: RecyclerItem): Boolean {
        Timber.e("areContentsTheSame called")
        return oldItem.id==newItem.id
    }
}

class NoSuchRecyclerItemTypeException:RuntimeException()
class NoSuchRecyclerViewTypeException:RuntimeException()

abstract class Cell<T>{
    abstract fun belongsTo(item:T?):Boolean
    abstract fun type():Int
    abstract fun holder(
            parent: ViewGroup, viewType: Int, viewModel: ViewModel?=null
    ):RecyclerView.ViewHolder
    abstract fun bind(
            holder: RecyclerView.ViewHolder,
            item: T?,
            listener: AdapterListener?
    )
    protected fun ViewGroup.viewOf(@LayoutRes resource:Int): View {
        return LayoutInflater.from(context).inflate(resource,this,false)
    }
}

class CellTypes<T>(vararg types: Cell<T>){
    private val cellTypes:ArrayList<Cell<T>> = ArrayList()
    init {
        types.forEach { addType(it)}
    }

    fun addType(type: Cell<T>) {
        cellTypes.add(type)
    }

    fun of(item:T?): Cell<T> {
        for (cellType in cellTypes){
            if (cellType.belongsTo(item)) return cellType
        }
        throw NoSuchRecyclerItemTypeException()
    }

    //this method make layout can not be reuse
    fun of(viewType:Int): Cell<T> {
        for (cellType in cellTypes){
            if (cellType.type()==viewType) return cellType
        }
        throw NoSuchRecyclerViewTypeException()
    }

}