package com.applications.toms.depormas.ui.screens.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.applications.toms.depormas.R
import com.applications.toms.depormas.databinding.FragmentSplashBinding
import com.applications.toms.depormas.utils.onBoardingFinished

class SplashFragment : Fragment() {

    private lateinit var binding: FragmentSplashBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_splash,container,false)

        if (onBoardingFinished(requireContext())) {
            // TODO GO TO HOME
        }else{
            loadSplashScreen()
        }

        return binding.root
    }

    private fun loadSplashScreen(){
        Handler(Looper.getMainLooper()).postDelayed({
            // You can declare your desire activity here to open after finishing splash screen. Like MainActivity
            // TODO GO TO OnBoardingViewPager
        },TIME_OUT)
    }

    companion object {
        private const val TAG = "SplashFragment"
        const val TIME_OUT:Long = 4000
    }
}