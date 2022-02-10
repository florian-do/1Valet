package com.do_f.a1valet

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.do_f.a1valet.extensions.setImageUrl

@BindingAdapter("android:visibility")
fun View.setVisibility(b: Boolean) {
    visibility = when (b) {
        true -> View.VISIBLE
        else -> View.GONE
    }
}

@BindingAdapter("android:background")
fun ImageView.drawableBackground(drawableId: Int) {
    setBackgroundResource(drawableId)
}

@BindingAdapter("app:imageUrl")
fun ImageView.appImageUrl(url: String?) {
    setImageUrl(url)
}