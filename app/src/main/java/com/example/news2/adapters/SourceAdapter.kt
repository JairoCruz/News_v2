package com.example.news2.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.news2.R
import com.example.news2.databinding.SourceItemBinding
import com.example.news2.model.SourceDomain

private const val ITEM_VIEW_TYPE_HEADER = 0
private const val ITEM_VIEW_TYPE_ITEM = 1

class SourceAdapter() : ListAdapter<DataItem, RecyclerView.ViewHolder>(SourceDiffCallback()) {

    init {
        setHasStableIds(true)
    }

    var tracker: SelectionTracker<Long>? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val withDataBinding: SourceItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), SourceViewHolder.LAYOUT, parent, false)
        //return SourceViewHolder(withDataBinding)
        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> HeaderViewHolder.from(parent)
            ITEM_VIEW_TYPE_ITEM -> SourceViewHolder(withDataBinding)

            else -> throw ClassCastException("Unknown viewType ${viewType}")
        }
    }

    // this function is used for build Header and list item
    fun addHeaderAndSubmitList(list: List<SourceDomain>?) {
        val items = when (list) {
            null -> listOf(DataItem.Header)
            else -> listOf(DataItem.Header) + list.map { DataItem.SourceDomainItem(it) }
        }
        submitList(items)
    }


    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItem.Header -> ITEM_VIEW_TYPE_HEADER
            is DataItem.SourceDomainItem -> ITEM_VIEW_TYPE_ITEM
        }
    }

    override fun getItemId(position: Int): Long = position.toLong()

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (holder) {
            is SourceViewHolder -> {
                val sourceItem = getItem(position) as DataItem.SourceDomainItem
                tracker?.let {
                    holder.bin(it.isSelected(position.toLong()))
                    holder.viewDataBinding.also { sib: SourceItemBinding ->
                        sib.source = sourceItem.sourceDomain
                    }
                }

            }
        }

    }

}

// This class is for to use one Header in the RecyclerView
class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    companion object {
        fun from(parent: ViewGroup): HeaderViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.header, parent, false)
            return HeaderViewHolder(view)
        }
    }
}

// This class is for to use one item list in the RecyclerView
class SourceViewHolder(val viewDataBinding: SourceItemBinding) : RecyclerView.ViewHolder(viewDataBinding.root) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.source_item
    }

    // This function is for activated the view and show selected one item for user
    fun bin(isActivated: Boolean = false) {
        itemView.isActivated = isActivated
    }

    // This function is for used Recycler-selection
    // https://proandroiddev.com/a-guide-to-recyclerview-selection-3ed9f2381504
    fun getItemDetails(): ItemDetailsLookup.ItemDetails<Long> =
            object : ItemDetailsLookup.ItemDetails<Long>() {
                override fun getSelectionKey(): Long? = itemId

                override fun getPosition(): Int = adapterPosition

            }
}


// This class is for avoid use notifydatachange in RecyclerView
class SourceDiffCallback : DiffUtil.ItemCallback<DataItem>() {
    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        // id is Long. idSource is id from source data
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem == newItem
    }

}


// This class is for Add Header to Recycler View
sealed class DataItem {

    abstract val id: Long

    data class SourceDomainItem(val sourceDomain: SourceDomain) : DataItem() {
        override val id = sourceDomain.id
    }

    object Header : DataItem() {
        override val id = Long.MIN_VALUE
    }

}