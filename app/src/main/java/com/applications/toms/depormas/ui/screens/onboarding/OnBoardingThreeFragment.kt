package com.applications.toms.depormas.ui.screens.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.applications.toms.depormas.R
import com.applications.toms.depormas.databinding.FragmentOnboardingThreeBinding
import com.applications.toms.depormas.utils.onFinishOnBoarding

class OnBoardingThreeFragment : Fragment() {

    private lateinit var binding: FragmentOnboardingThreeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_onboarding_three,container,false)

        binding.onboardingThreeBtn.setOnClickListener {
            // TODO GO TO HOME
            onBoardingFinish()
        }

        return binding.root
    }

    private fun onBoardingFinish(){
        onFinishOnBoarding(requireContext())
    }

    companion object {
        private const val TAG = "OnboardingThreeFragment"
    }
}