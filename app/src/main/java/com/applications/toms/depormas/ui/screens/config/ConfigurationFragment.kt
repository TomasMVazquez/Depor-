package com.applications.toms.depormas.ui.screens.config

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.applications.toms.depormas.R
import com.applications.toms.depormas.databinding.FragmentConfigurationBinding
import com.applications.toms.depormas.utils.getDarkMode
import com.applications.toms.depormas.utils.setConstraintStatusBarMargin

class ConfigurationFragment : Fragment() {

    private lateinit var binding: FragmentConfigurationBinding
    private lateinit var viewModelConfig: ConfigurationViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_configuration, container, false)

        viewModelConfig = ViewModelProvider(this).get(ConfigurationViewModel::class.java)

        binding.viewModelConfig = viewModelConfig

        setConstraintStatusBarMargin(requireContext(), binding.container)

        viewModelConfig.darkMode.observe(viewLifecycleOwner, Observer { isDarkMode ->
            if (isDarkMode != null) viewModelConfig.setSelectedMode()
        })

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
