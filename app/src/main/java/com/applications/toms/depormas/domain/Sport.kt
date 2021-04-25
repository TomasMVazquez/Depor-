package com.applications.toms.depormas.domain

import android.content.Context
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Sport(
    val id: Int = -1,
    val name: String = "",
    val img: String = "",
    val max_players: Int = -1,
    var choosen: Boolean = false
): Parcelable{

    fun getDrawableInt(context: Context): Int = context.resources.getIdentifier(img,"drawable",context.packageName)

}