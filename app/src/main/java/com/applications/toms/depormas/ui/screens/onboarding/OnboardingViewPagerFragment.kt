package com.applications.toms.depormas.ui.screens.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.applications.toms.depormas.R
import com.applications.toms.depormas.databinding.FragmentOnboardingViewPagerBinding
import com.applications.toms.depormas.ui.adapters.ViewPagerAdapter

class OnboardingViewPagerFragment : Fragment() {

    private lateinit var binding: FragmentOnboardingViewPagerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_onboarding_view_pager,container,false)

        val fragmentList = arrayListOf<Fragment>(
            OnBoardingOneFragment(),
            OnBoardingTwoFragment(),
            OnBoardingThreeFragment()
        )

        val viewPagerAdapter = ViewPagerAdapter(fragmentList, requireActivity().supportFragmentManager,lifecycle)

        binding.onboardingViewPager.adapter = viewPagerAdapter

        return binding.root
    }

    companion object {
        private const val TAG = "OnboardingViewPagerFrag"
    }
}