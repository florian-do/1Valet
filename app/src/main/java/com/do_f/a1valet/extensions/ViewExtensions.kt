package com.do_f.a1valet.extensions

import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.do_f.a1valet.listener.SafeClickListener

fun View.setSafeOnClickListener(onSafeClick: (View) -> Unit) {
    val safeClickListener = SafeClickListener {
        onSafeClick(it)
    }
    setOnClickListener(safeClickListener)
}

fun ImageView.setImageUrl(imageUrl: String?) {
    imageUrl?.let {
        Glide.with(this)
            .load(it)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .into(this)
    }
}