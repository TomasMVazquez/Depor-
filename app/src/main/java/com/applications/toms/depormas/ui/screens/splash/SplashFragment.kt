package com.applications.toms.depormas.ui.screens.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.applications.toms.depormas.MainActivity
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

        Handler(Looper.getMainLooper()).postDelayed({
            if (onBoardingFinished(requireContext())) {
                goToMainActivity()
            }else{
                loadSplashScreen()
            }
        },TIME_OUT)
        
        return binding.root
    }

    private fun goToMainActivity() {
        val intent = Intent(requireActivity(),MainActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    private fun loadSplashScreen(){
        findNavController().navigate(R.id.action_splashFragment_to_onboardingViewPagerFragment)
    }

    companion object {
        private const val TAG = "SplashFragment"
        const val TIME_OUT:Long = 4000
    }
}