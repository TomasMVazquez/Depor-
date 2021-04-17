package com.applications.toms.depormas.ui.screens.home

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.applications.toms.depormas.R
import com.applications.toms.depormas.domain.Sport
import com.applications.toms.depormas.data.repository.SportRepository
import com.applications.toms.depormas.data.database.remote.Network
import com.applications.toms.depormas.data.database.local.RoomDataSource
import com.applications.toms.depormas.data.database.local.SportDatabase
import com.applications.toms.depormas.databinding.FragmentHomeBinding
import com.applications.toms.depormas.ui.adapters.SportAdapter
import com.applications.toms.depormas.ui.adapters.SportListener
import com.applications.toms.depormas.ui.screens.home.HomeViewModel.UiModel.Content
import com.applications.toms.depormas.ui.screens.home.HomeViewModel.UiModel.Loading
import com.applications.toms.depormas.usecases.GetSports
import com.applications.toms.depormas.utils.getViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi

@ExperimentalCoroutinesApi
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeViewModel: HomeViewModel

    private val sportAdapter by lazy { SportAdapter(SportListener { sport ->
        updateAdapter(sport)
    }) }

    @InternalCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_home, container, false)

        setHasOptionsMenu(true)

        val sportDatabase = SportDatabase.getInstance(requireContext())
        val sportRepository = SportRepository(lifecycleScope, RoomDataSource(sportDatabase), Network())
        val getSport = GetSports(sportRepository)

        homeViewModel = getViewModel { HomeViewModel(getSport) }

        binding.homeViewModel = homeViewModel

        binding.sportRecycler.adapter = sportAdapter

        homeViewModel.sports.observe(viewLifecycleOwner){
            sportAdapter.submitList(it)
        }

        homeViewModel.selectedSport.observe(viewLifecycleOwner){
            if (it != null) {
                binding.dashboardImg.setImageResource(it.getDrawableInt(binding.dashboardImg.context))
            }
        }

        homeViewModel.model.observe(viewLifecycleOwner, ::updateUi)

        return binding.root
    }

    private fun updateUi(model: HomeViewModel.UiModel){
        binding.progressBar.visibility = if (model == Loading) View.VISIBLE else View.GONE
        when (model){
            is Content -> {
                Log.d(TAG, "updateUi: ${model.events}")
                if (model.events.isEmpty()) {
                    binding.emptyStateGroup.visibility = View.VISIBLE
                    binding.eventsGroup.visibility = View.GONE
                }else {
                    binding.emptyStateGroup.visibility = View.GONE
                    binding.eventsGroup.visibility = View.VISIBLE
                }
            }
            else -> Log.d(TAG, "else")
        }
    }

    private fun updateAdapter(sportChecked: Sport) {
        sportAdapter.currentList.map { sport ->
            if (sport.id != sportChecked.id && sport.choosen) sport.choosen = false
        }
        sportChecked.choosen = !sportChecked.choosen
        if (sportChecked.choosen) {
            homeViewModel.onSelectSport(sportChecked)
        }else {
            homeViewModel.onSelectSport(null)
        }
        sportAdapter.notifyDataSetChanged()
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
