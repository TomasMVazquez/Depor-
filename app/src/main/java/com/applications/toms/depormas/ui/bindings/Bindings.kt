package com.applications.toms.depormas.ui.bindings

import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.applications.toms.depormas.R
import com.applications.toms.depormas.data.model.Sport
import com.bumptech.glide.Glide

@BindingAdapter("imageSrcDarkLight")
fun bindImageDarkOrLight(imageView: ImageView,isDarkMode: Boolean){
    if (isDarkMode){
        imageView.setImageDrawable(ContextCompat.getDrawable(imageView.context, R.drawable.depormas_icon_colored_dark_big))
    }else{
        imageView.setImageDrawable(ContextCompat.getDrawable(imageView.context, R.drawable.depormas_icon_colored_light_big))
    }
}

@BindingAdapter("sportImg")
fun bindSportImage(imageView: ImageView, sport: Sport){
    imageView.setImageResource(sport.getDrawableInt(imageView.context))
}