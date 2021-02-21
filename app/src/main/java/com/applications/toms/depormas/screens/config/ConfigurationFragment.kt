package com.applications.toms.depormas.screens.config

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.marginTop
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavHostController
import androidx.navigation.fragment.NavHostFragment
import com.applications.toms.depormas.R
import com.applications.toms.depormas.databinding.FragmentConfigurationBinding
import com.applications.toms.depormas.utils.setConstraintStatusBarMargin

class ConfigurationFragment : Fragment() {

    private lateinit var binding: FragmentConfigurationBinding
    // FORCE NIGHT MODE
//    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//    >= Android version Q
//    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_configuration, container, false)

        setConstraintStatusBarMargin(requireContext(),binding.container)

        binding.goToAbout.setOnClickListener {
            val action = ConfigurationFragmentDirections.actionConfigurationFragmentToAboutUsFragment()
            NavHostFragment.findNavController(this).navigate(action)
        }

        return binding.root
    }

    companion object {
        private const val TAG = "ConfigurationFragment"
    }
}