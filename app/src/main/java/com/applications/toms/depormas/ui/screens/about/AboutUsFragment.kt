package com.applications.toms.depormas.ui.screens.about

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.applications.toms.depormas.R
import com.applications.toms.depormas.databinding.FragmentAboutUsBinding
import com.applications.toms.depormas.utils.getDarkMode
import com.applications.toms.depormas.utils.setConstraintStatusBarMargin

class AboutUsFragment : Fragment() {

    private lateinit var binding: FragmentAboutUsBinding
    private lateinit var viewModelAbout: ViewModelAboutUs

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_about_us, container, false)

        viewModelAbout = ViewModelProvider(this).get(ViewModelAboutUs::class.java)

        binding.viewModelAbout = viewModelAbout

        viewModelAbout.getDarkMode(getDarkMode(requireContext()))

        setConstraintStatusBarMargin(requireContext(), binding.container)

        binding.thanks.setAttributions(resources.getTextArray(R.array.attributions))

        return binding.root
    }

    companion object {
        private const val TAG = "AboutUsFragment"
    }
}
