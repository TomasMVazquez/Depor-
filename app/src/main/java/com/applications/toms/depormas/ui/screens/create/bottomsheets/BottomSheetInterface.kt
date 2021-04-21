package com.applications.toms.depormas.ui.screens.create.bottomsheets

import com.applications.toms.depormas.domain.Location


interface BottomSheetInterface {
    fun getDataFromBottomSheet(code: Int,data: String)
}

interface BottomSheetMapInterface {
    fun getDataFromMapBottomSheet(code: Int,data: Location)
}