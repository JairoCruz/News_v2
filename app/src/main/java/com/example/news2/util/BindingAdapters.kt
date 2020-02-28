package com.example.news2.util

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

/**
 * Binding adapter used to hide the spinner once data is available
 */
@BindingAdapter("isNetworkError", "sources")
fun hideIfNetworkError(view: View, isNetWorkError: Boolean, sources: Any? ){
    view.visibility = if (sources != null) View.GONE else View.VISIBLE
    if(isNetWorkError) {
        view.visibility = View.GONE
    }


}

@BindingAdapter("imageUrl")
fun setImageUrl(imageView: ImageView, url: String) {
    Glide.with(imageView.context).load(url).into(imageView)
}

