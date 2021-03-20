package com.applications.toms.depormas.ui.screens.home

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.applications.toms.depormas.R
import com.applications.toms.depormas.databinding.FragmentHomeBinding
import com.applications.toms.depormas.data.repository.SportRepository
import com.applications.toms.depormas.data.source.Network
import com.applications.toms.depormas.data.source.database.RoomDataSource
import com.applications.toms.depormas.data.source.database.SportDatabase
import com.applications.toms.depormas.ui.adapters.SportAdapter
import com.applications.toms.depormas.ui.adapters.SportListener
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi

@ExperimentalCoroutinesApi
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeViewModel: HomeViewModel

    @InternalCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_home, container, false)

        setHasOptionsMenu(true)

        val sportRepository = SportRepository(RoomDataSource(requireContext()),Network())

        homeViewModel = ViewModelProvider(this,HomeViewModelFactory(sportRepository))
            .get(HomeViewModel::class.java)

        val sportAdapter = SportAdapter(SportListener { sport ->
            sport.choosen = !sport.choosen
            Toast.makeText(requireContext(),"${sport.name} + ${sport.choosen}",Toast.LENGTH_SHORT).show()
        })

        binding.sportRecycler.adapter = sportAdapter

        homeViewModel.sports.observe(viewLifecycleOwner){
            sportAdapter.submitList(it)
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item, NavHostFragment.findNavController(this))
                || super.onOptionsItemSelected(item)
    }

    companion object {
        private const val TAG = "HomeFragment"
    }
}
