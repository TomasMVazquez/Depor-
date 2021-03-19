package com.applications.toms.depormas.model

import android.content.Context
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Sport(
    val id: Int?,
    val name: String?,
    val img: String?,
    val max_players: Int?
): Parcelable{

    constructor() : this(null,null,null,null)

    fun getDrawableInt(context: Context): Int = context.resources.getIdentifier(name,img,context.packageName)

}