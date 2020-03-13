package com.example.news2.adapters

import android.util.Log
import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView

class MyItemDetailsLookup(private val recyclerView: RecyclerView) : ItemDetailsLookup<Long>() {
    override fun getItemDetails(e: MotionEvent): ItemDetails<Long>? {
        val view = recyclerView.findChildViewUnder(e.x, e.y)
        if (view != null) {
            //Log.e("MyItemDetailsLookup", "Esto no es null")
            return (recyclerView.getChildViewHolder(view) as SourceViewHolder).getItemDetails()
        }
        return null
    }

}