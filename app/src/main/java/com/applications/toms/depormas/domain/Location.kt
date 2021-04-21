package com.applications.toms.depormas.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Location (
    val id: Int? = -1,
    val name: String = "",
    val address: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val city: String = "",
    val countryCode: String = ""
): Parcelable