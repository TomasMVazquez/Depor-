package com.applications.toms.depormas.data.model

import android.content.Context
import android.os.Parcelable
import com.applications.toms.depormas.data.source.database.SportDbItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class Sport(
    val id: Int?,
    val name: String?,
    val img: String?,
    val max_players: Int?,
    var choosen: Boolean = false
): Parcelable{

    constructor() : this(null,null,null,null)

    fun getDrawableInt(context: Context): Int = context.resources.getIdentifier(img,"drawable",context.packageName)

}

fun Sport.asDatabaseModel(): SportDbItem{
    return SportDbItem(id, name, img, max_players)
}

fun List<Sport>.asDatabaseModel(): Array<SportDbItem>{
    return map{
        SportDbItem(
            id = it.id,
            name = it.name,
            img = it.img,
            max_players = it.max_players
        )
    }.toTypedArray()
}
