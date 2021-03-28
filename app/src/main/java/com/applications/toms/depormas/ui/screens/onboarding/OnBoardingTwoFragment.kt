package com.applications.toms.depormas.ui.screens.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import com.applications.toms.depormas.R
import com.applications.toms.depormas.databinding.FragmentOnboardingTwoBinding

class OnBoardingTwoFragment : Fragment() {

    private lateinit var binding: FragmentOnboardingTwoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_onboarding_two,container,false)

        val viewPager = activity?.findViewById<ViewPager2>(R.id.onboardingViewPager)

        binding.onboardingTwoBtn.setOnClickListener {
            viewPager?.currentItem = 2
        }

        return binding.root
    }

    companion object {
        private const val TAG = "OnboardingTwoFragment"
    }
}