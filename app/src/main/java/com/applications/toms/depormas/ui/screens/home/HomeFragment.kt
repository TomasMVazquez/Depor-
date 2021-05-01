package com.applications.toms.depormas.ui.screens.home

import android.Manifest.permission.*
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.applications.toms.depormas.R
import com.applications.toms.depormas.data.AndroidPermissionChecker
import com.applications.toms.depormas.data.PlayServicesLocationDataSource
import com.applications.toms.depormas.data.database.local.RoomEventDataSource
import com.applications.toms.depormas.data.database.local.RoomFavoriteDataSource
import com.applications.toms.depormas.domain.Sport
import com.applications.toms.depormas.data.repository.SportRepository
import com.applications.toms.depormas.data.database.remote.SportFirestoreServer
import com.applications.toms.depormas.data.database.local.RoomSportDataSource
import com.applications.toms.depormas.data.database.local.event.EventDatabase
import com.applications.toms.depormas.data.database.local.favorite.FavoriteDatabase
import com.applications.toms.depormas.data.database.local.sport.SportDatabase
import com.applications.toms.depormas.data.database.remote.EventFirestoreServer
import com.applications.toms.depormas.data.repository.EventRepository
import com.applications.toms.depormas.data.repository.FavoriteRepository
import com.applications.toms.depormas.data.repository.LocationRepository
import com.applications.toms.depormas.databinding.FragmentHomeBinding
import com.applications.toms.depormas.domain.Event
import com.applications.toms.depormas.domain.filterBySport
import com.applications.toms.depormas.ui.adapters.EventAdapter
import com.applications.toms.depormas.ui.adapters.EventListener
import com.applications.toms.depormas.ui.adapters.SportAdapter
import com.applications.toms.depormas.ui.adapters.SportListener
import com.applications.toms.depormas.usecases.*
import com.applications.toms.depormas.utils.getViewModel
import com.applications.toms.depormas.utils.snackBar
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect

@ExperimentalCoroutinesApi
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeViewModel: HomeViewModel

    private lateinit var myLocation: Map<String, Double>

    private val coarseLocationPermissionRequester =
        registerForActivityResult(ActivityResultContracts.RequestPermission()){ granted ->
            when{
                granted -> homeViewModel.onCoarseLocationPermissionRequested()
                shouldShowRequestPermissionRationale(ACCESS_COARSE_LOCATION) -> Log.d(TAG, "rational") //TODO rational
                else -> Log.d(TAG, "denied")//TODO filter depending on permission granted/denied
            }

        }

    private val sportAdapter by lazy { SportAdapter(SportListener { sport ->
        sport.choosen = !sport.choosen
        updateAdapter(sport)
    }) }

    private val eventAdapter by lazy {
        EventAdapter(
                EventListener { event ->
                    Toast.makeText(requireContext(), event.event_name,Toast.LENGTH_SHORT).show()
                }
        )
    }

    private lateinit var swipeIcon: Drawable

    private val onSwipeCallback = object: ItemTouchHelper.SimpleCallback(0,
            ItemTouchHelper.LEFT) {
        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean = false

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            eventAdapter.notifyItemChanged(viewHolder.adapterPosition)
            homeViewModel.onSwipeItemToAddToFavorite(eventAdapter.currentList[viewHolder.adapterPosition])
        }

        override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {

            c.clipRect(
                    viewHolder.itemView.right + dX.toInt(),
                    viewHolder.itemView.top,
                    viewHolder.itemView.right,
                    viewHolder.itemView.bottom
            )

            if(viewHolder.itemView.right + dX > viewHolder.itemView.width * 0.66)
                c.drawColor(ContextCompat.getColor(requireContext(),R.color.greyColor))
            else
                c.drawColor(ContextCompat.getColor(requireContext(),R.color.greenLightColor))

            val iconMargin = (viewHolder.itemView.height - swipeIcon!!.intrinsicHeight) / 2

            swipeIcon!!.bounds = Rect(
                    viewHolder.itemView.right - iconMargin - swipeIcon!!.intrinsicWidth,
                    viewHolder.itemView.top + iconMargin,
                    viewHolder.itemView.right - iconMargin,
                    viewHolder.itemView.bottom - iconMargin
            )

            swipeIcon!!.draw(c)

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }
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

        homeViewModel = getViewModel(::buildViewModel)

        binding.homeViewModel = homeViewModel

        binding.sportRecycler.adapter = sportAdapter
        binding.eventRecycler.adapter = eventAdapter

        swipeIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_save_favorite)!!

        val myHelper = ItemTouchHelper(onSwipeCallback)
        myHelper.attachToRecyclerView(binding.eventRecycler)

        lifecycleScope.launchWhenStarted {
            getMyLocation()
            homeViewModel.selectedSport.collect {
                binding.dashboardImg.setImageResource(it.getDrawableInt(binding.dashboardImg.context))
            }
        }

        homeViewModel.sports.observe(viewLifecycleOwner){
            sportAdapter.submitList(it)
        }

        homeViewModel.events.observe(viewLifecycleOwner,::updateUi)

        homeViewModel.onFavoriteSaved.observe(viewLifecycleOwner){ event ->
            event.getContentIfNotHandled().let {
                when(it){
                  0 -> binding.root.snackBar(getString(R.string.favorite_already_saved))
                  1 -> binding.root.snackBar(getString(R.string.favorite_saved_msg))
                }
            }
        }

        return binding.root
    }

    private fun buildViewModel(): HomeViewModel {
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

        val favoriteRepository = FavoriteRepository(
                RoomFavoriteDataSource(
                        FavoriteDatabase.getInstance(requireContext())
                )
        )

        val saveFavorite = SaveFavorite(favoriteRepository)

        val getFavorite = GetFavorites(favoriteRepository)

        return getViewModel { HomeViewModel(getSport, getEvents, getFavorite, saveFavorite) }
    }

    private suspend fun getMyLocation() {
        myLocation = GetMyLocation(
                LocationRepository(
                        PlayServicesLocationDataSource(requireContext()),
                        AndroidPermissionChecker(requireContext())
                )
        ).invoke()
        eventAdapter.myLocation = myLocation
        eventAdapter.notifyDataSetChanged()
    }

    private fun updateUi(events: List<Event>){
        binding.progressBar.visibility = View.GONE
        if (events.isNullOrEmpty()) {
            eventAdapter.submitList(emptyList())
            binding.emptyStateGroup.visibility = View.VISIBLE
            binding.eventsGroup.visibility = View.GONE
        } else {
            eventAdapter.submitList(events)
            binding.dashboardCount.text = String.format(getString(R.string.dashboard_count_events), events.size);
            binding.emptyStateGroup.visibility = View.GONE
            binding.eventsGroup.visibility = View.VISIBLE
        }
    }

    private fun updateAdapter(sportChecked: Sport) {
        sportAdapter.currentList.map {
            if (it.id != sportChecked.id && it.choosen) it.choosen = false
        }

        if (sportChecked.choosen) {
            homeViewModel.onSelectSport(sportChecked)
            if (!sportAdapter.currentList.first { it.id == sportChecked.id }.choosen)
                sportAdapter.currentList.first { it.id == sportChecked.id }.choosen = true
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
