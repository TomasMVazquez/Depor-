package com.applications.toms.depormas.ui.screens.home

import android.Manifest.permission.*
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.applications.toms.depormas.R
import com.applications.toms.depormas.data.AndroidPermissionChecker
import com.applications.toms.depormas.data.PlayServicesLocationDataSource
import com.applications.toms.depormas.data.database.local.RoomEventDataSource
import com.applications.toms.depormas.domain.Sport
import com.applications.toms.depormas.data.repository.SportRepository
import com.applications.toms.depormas.data.database.remote.SportFirestoreServer
import com.applications.toms.depormas.data.database.local.RoomSportDataSource
import com.applications.toms.depormas.data.database.local.event.EventDatabase
import com.applications.toms.depormas.data.database.local.sport.SportDatabase
import com.applications.toms.depormas.data.database.remote.EventFirestoreServer
import com.applications.toms.depormas.data.repository.EventRepository
import com.applications.toms.depormas.data.repository.LocationRepository
import com.applications.toms.depormas.databinding.FragmentHomeBinding
import com.applications.toms.depormas.ui.adapters.EventAdapter
import com.applications.toms.depormas.ui.adapters.EventListener
import com.applications.toms.depormas.ui.adapters.SportAdapter
import com.applications.toms.depormas.ui.adapters.SportListener
import com.applications.toms.depormas.ui.screens.home.HomeViewModel.UiModel
import com.applications.toms.depormas.ui.screens.home.HomeViewModel.UiModel.*
import com.applications.toms.depormas.usecases.GetEvents
import com.applications.toms.depormas.usecases.GetMyLocation
import com.applications.toms.depormas.usecases.GetSports
import com.applications.toms.depormas.utils.getViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeViewModel: HomeViewModel

    private val coarseLocationPermissionRequester =
        registerForActivityResult(ActivityResultContracts.RequestPermission()){ granted ->
            when{
                granted -> homeViewModel.onCoarseLocationPermissionRequested()
                shouldShowRequestPermissionRationale(ACCESS_COARSE_LOCATION) -> Log.d(TAG, "rational") //TODO rational
                else -> Log.d(TAG, "denied")//TODO filter depending on permission granted/denied
            }

        }

    private val sportAdapter by lazy { SportAdapter(SportListener { sport ->
        updateAdapter(sport)
    }) }

    private val eventAdapter by lazy {
        EventAdapter(
                EventListener { event ->
                    Toast.makeText(requireContext(),"${event.event_name}",Toast.LENGTH_SHORT).show()
                }
        )
    }

    @InternalCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_home, container, false)

        setHasOptionsMenu(true)

        val getSport = GetSports(
                SportRepository(
                        lifecycleScope,
                        RoomSportDataSource(SportDatabase.getInstance(requireContext())),
                        SportFirestoreServer()
                )
        )

        val getEvents = GetEvents(
                EventRepository(
                        lifecycleScope,
                        RoomEventDataSource(EventDatabase.getInstance(requireContext())),
                        EventFirestoreServer()
                )
        )

        homeViewModel = getViewModel { HomeViewModel(getSport,getEvents) }

        binding.homeViewModel = homeViewModel

        binding.sportRecycler.adapter = sportAdapter
        binding.eventRecycler.adapter = eventAdapter

        lifecycleScope.launch {
            eventAdapter.myLocation =  GetMyLocation(
                    LocationRepository(
                            PlayServicesLocationDataSource(requireContext()),
                            AndroidPermissionChecker(requireContext())
                    )
            ).invoke()
            eventAdapter.notifyDataSetChanged()
        }

        homeViewModel.sports.observe(viewLifecycleOwner){
            sportAdapter.submitList(it)
        }

        homeViewModel.selectedSport.observe(viewLifecycleOwner){
            if (it != null) {
                binding.dashboardImg.setImageResource(it.getDrawableInt(binding.dashboardImg.context))
                homeViewModel.onFilterEventsBySportSelected(it)
            }
        }

        homeViewModel.model.observe(viewLifecycleOwner, ::updateUi)

        return binding.root
    }

    private fun updateUi(model: UiModel){
        binding.progressBar.visibility = if (model == Loading) View.VISIBLE else View.GONE
        when (model){
            is Content -> {
                if (model.events.isNullOrEmpty()) {
                    eventAdapter.submitList(emptyList())
                    binding.emptyStateGroup.visibility = View.VISIBLE
                    binding.eventsGroup.visibility = View.GONE
                } else {
                    eventAdapter.submitList(model.events)
                    binding.dashboardCount.text = String.format(getString(R.string.dashboard_count_events), model.events.size);
                    binding.emptyStateGroup.visibility = View.GONE
                    binding.eventsGroup.visibility = View.VISIBLE
                }
            }
            RequestLocationPermission -> coarseLocationPermissionRequester.launch(ACCESS_COARSE_LOCATION)
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
