package com.applications.toms.depormas

import android.app.Application
import com.applications.toms.depormas.utils.SharedPreferences

val preferences: SharedPreferences by lazy {
    DeporMasApplication.preferences!!
}

class DeporMasApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        initDI()
        instance = this
        preferences = SharedPreferences(applicationContext)
    }

    companion object{
        var preferences: SharedPreferences? = null
        lateinit var instance: DeporMasApplication
            private set
    }
}
