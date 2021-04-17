package com.applications.toms.depormas.ui.bindings

import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.applications.toms.depormas.R
import com.applications.toms.depormas.domain.Sport

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