package com.example.news2.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.news2.R
import com.example.news2.SourceFragment
import com.example.news2.databinding.SourceItemBinding
import com.example.news2.model.SourceDomain
import kotlinx.android.synthetic.main.source_item.view.*

class SourceAdapter(val callBack: SourceFragment.CheckBoxClick) : RecyclerView.Adapter<SourceViewHolder>() {

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

    override fun onBindViewHolder(holder: SourceViewHolder, position: Int) {
        holder.viewDataBinding.also {
            // Log.e("Adpater2", "sera")
            it.source = sources[position]



            it.clickCheckBox = callBack

          /* it.checkBox.setOnClickListener { v ->
                if(!sources[position].isChecked){
                    v.checkBox.isChecked = true

                    sources[position].isChecked = true
                } else {
                    v.checkBox.isChecked = false
                    sources[position].isChecked = false
                }
            }


            */

        }
    }

}

class SourceViewHolder(val viewDataBinding: SourceItemBinding) : RecyclerView.ViewHolder(viewDataBinding.root) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.source_item
    }
}