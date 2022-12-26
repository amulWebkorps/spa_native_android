package com.example.spa.utilities

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

object GlideUtils {

    fun loadImage(
        context: Context?,
        resourceUrl: String,
        placeHolderId: Int,
        errorResId: Int,
        imageView: ImageView
    ) {
        context?.let {
            Glide
                .with(it)
                .load(resourceUrl)
                .placeholder(placeHolderId)
                .into(imageView)

            /* Glide
                .with(it)
                .load(if (isThumbnail) Utils.getThumbImage(resourceUrl) else resourceUrl)
                .placeholder(ContextCompat.getDrawable(it, placeHolderId))
                .error(ContextCompat.getDrawable(it, errorResId))
                .into(imageView)*/
        }
    }

    fun loadImage(
        context: Context?,
        resourceUrl: String,
        placeHolderId: Drawable?,
        errorResId: Drawable?,
        imageView: ImageView
    ) {
        context?.let {
            Glide
                .with(it)
                .load(resourceUrl)
                .placeholder(placeHolderId)
                .error(errorResId)
                .into(imageView)
        }
    }

}

fun ImageView.loadImage(resource: Any) {
    Glide.with(context)
        .load(resource)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(this)
}