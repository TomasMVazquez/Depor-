package com.applications.toms.depormas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.findNavController
import com.applications.toms.depormas.customviews.bottomnavigationview.CbnMenuItem
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Navigation actions Bar
        val navController = this.findNavController(R.id.myNavHostFragment)
        //NavigationUI.setupActionBarWithNavController(this, navController)
        val menuItems = arrayOf(
            CbnMenuItem(
                R.drawable.ic_action_fav, // the icon
                R.drawable.avd_fav, // the AVD that will be shown in FAB
                R.id.favouriteFragment // optional if you use Jetpack Navigation
            ),
            CbnMenuItem(
                R.drawable.ic_action_home,
                R.drawable.avd_home,
                R.id.homeFragment
            ),
            CbnMenuItem(
                R.drawable.ic_action_create,
                R.drawable.avd_create,
                R.id.createEventFragment
            )
        )
        bottom_navigation_view.setMenuItems(menuItems, 1)
        bottom_navigation_view.setupWithNavController(navController)
    }
}