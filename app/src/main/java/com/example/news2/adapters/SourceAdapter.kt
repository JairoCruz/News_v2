package com.example.news2.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.RecyclerView
import com.example.news2.R
import com.example.news2.SourceFragment
import com.example.news2.databinding.SourceItemBinding
import com.example.news2.model.SourceDomain
import kotlinx.android.synthetic.main.source_item.view.*

class SourceAdapter() : RecyclerView.Adapter<SourceViewHolder>() {

    init {
        setHasStableIds(true)
    }
    var tracker: SelectionTracker<Long>? = null
    /**
     * The sources that our Adapter will show
     *
     */
    var sources: List<SourceDomain> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SourceViewHolder {
       val withDataBinding: SourceItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), SourceViewHolder.LAYOUT, parent, false)
        return SourceViewHolder(withDataBinding)
    }

    override fun getItemCount(): Int {
        return sources.size
    }

    override fun getItemId(position: Int): Long = position.toLong()

    override fun onBindViewHolder(holder: SourceViewHolder, position: Int) {

        tracker?.let {
            //Log.e("Sour","select: ${it.isSelected(position.toLong())}")
            holder.bin(it.isSelected(position.toLong()))
            holder.viewDataBinding.also { sib: SourceItemBinding ->
                // Log.e("Adpater2", "sera")
                sib.source = sources[position]
            }
        }

    }

}

class SourceViewHolder(val viewDataBinding: SourceItemBinding) : RecyclerView.ViewHolder(viewDataBinding.root) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.source_item

    }

    fun bin(isActivated: Boolean = false) {
        //Log.e("SourceAdapter", "Estoy: $isActivated")
        itemView.isActivated = isActivated

    }

    fun getItemDetails(): ItemDetailsLookup.ItemDetails<Long> =
        object : ItemDetailsLookup.ItemDetails<Long>() {
            override fun getSelectionKey(): Long? = itemId

            override fun getPosition(): Int = adapterPosition

        }
}