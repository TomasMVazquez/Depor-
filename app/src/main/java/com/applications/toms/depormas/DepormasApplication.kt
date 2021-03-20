package com.applications.toms.depormas

import android.app.Application
import com.applications.toms.depormas.utils.SharedPreferencesKeys
import com.applications.toms.depormas.utils.getDarkMode
import com.applications.toms.depormas.utils.setSelectedMode

class DepormasApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        setSelectedMode(this.applicationContext)
    }
}
