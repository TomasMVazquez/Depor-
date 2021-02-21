package com.applications.toms.depormas.screens.notifications

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.applications.toms.depormas.R
import com.applications.toms.depormas.databinding.FragmentNotificationsBinding
import com.applications.toms.depormas.utils.setConstraintStatusBarMargin

class NotificationsFragment : Fragment() {

    private lateinit var binding: FragmentNotificationsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_notifications, container, false)

        setConstraintStatusBarMargin(requireContext(),binding.container)

        return binding.root
    }

    companion object {
        private const val TAG = "NotificationsFragment"
    }
}