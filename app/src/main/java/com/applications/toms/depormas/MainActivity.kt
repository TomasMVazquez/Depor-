package com.applications.toms.depormas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.applications.toms.depormas.databinding.ActivityMainBinding
import com.applications.toms.depormas.ui.customviews.bottomnavigationview.CbnMenuItem

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private val menuItems by lazy {
        arrayOf(
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
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = this.findNavController(R.id.myNavHostFragment)
        // To not show the back arrow in the fragments that are not hosting the nav
        val appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.homeFragment,
                    R.id.favouriteFragment,
                    R.id.createEventFragment
                )
        )
        NavigationUI.setupActionBarWithNavController(this,navController,appBarConfiguration)

        with(binding.bottomNavigationView){
            setMenuItems(menuItems,1)
            setupWithNavController(navController)
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.configurationFragment,
                R.id.notificationsFragment,
                R.id.aboutUsFragment -> hideBottomNav()
                else -> showBottomNav()
            }
        }
    }

    private fun showBottomNav() {
        binding.bottomNavigationView.visibility = View.VISIBLE
    }

    private fun hideBottomNav() {
        binding.bottomNavigationView.visibility = View.GONE
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.myNavHostFragment)
        return navController.navigateUp()
    }
}
