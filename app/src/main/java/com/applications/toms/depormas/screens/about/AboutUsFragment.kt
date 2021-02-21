package com.applications.toms.depormas.screens.about

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.applications.toms.depormas.R
import com.applications.toms.depormas.databinding.FragmentAboutUsBinding
import com.applications.toms.depormas.utils.setConstraintStatusBarMargin

class AboutUsFragment : Fragment() {

    private lateinit var binding: FragmentAboutUsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_about_us, container, false)

        setConstraintStatusBarMargin(requireContext(),binding.container)

        return binding.root
    }

    companion object {
        private const val TAG = "AboutUsFragment"
    }
}