package com.applications.toms.depormas.ui.screens.favourite

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.applications.toms.depormas.R
import com.applications.toms.depormas.data.database.local.RoomEventDataSource
import com.applications.toms.depormas.data.database.local.RoomFavoriteDataSource
import com.applications.toms.depormas.data.database.local.event.EventDatabase
import com.applications.toms.depormas.data.database.local.favorite.FavoriteDatabase
import com.applications.toms.depormas.data.database.remote.EventFirestoreServer
import com.applications.toms.depormas.data.repository.EventRepository
import com.applications.toms.depormas.data.repository.FavoriteRepository
import com.applications.toms.depormas.data.source.favorite.LocalFavoriteDataSource
import com.applications.toms.depormas.databinding.FragmentFavouriteBinding
import com.applications.toms.depormas.ui.adapters.FavoriteAdapter
import com.applications.toms.depormas.ui.adapters.FavoriteListener
import com.applications.toms.depormas.ui.screens.favourite.FavoriteViewModel.*
import com.applications.toms.depormas.ui.screens.favourite.FavoriteViewModel.UiModel.*
import com.applications.toms.depormas.usecases.GetEvents
import com.applications.toms.depormas.usecases.GetFavorites
import com.applications.toms.depormas.utils.getViewModel

class FavouriteFragment : Fragment() {

    private lateinit var binding: FragmentFavouriteBinding
    private lateinit var favoriteViewModel: FavoriteViewModel

    private val favoriteAdapter by lazy {
        FavoriteAdapter(FavoriteListener {
            Toast.makeText(requireContext(), it.event_name,Toast.LENGTH_SHORT).show()
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_favourite, container, false)

        setHasOptionsMenu(true)

        favoriteViewModel = getViewModel {
            FavoriteViewModel(
                    GetFavorites(
                            FavoriteRepository(
                                    RoomFavoriteDataSource(
                                            FavoriteDatabase.getInstance(requireContext())
                                    )
                            )
                    ),
                    GetEvents(
                            EventRepository(
                                    lifecycleScope,
                                    RoomEventDataSource(EventDatabase.getInstance(requireContext())),
                                    EventFirestoreServer()
                            )
                    )
            )
        }

        binding.favoritesRecycler.adapter = favoriteAdapter

        favoriteViewModel.events.observe(viewLifecycleOwner){
            favoriteViewModel.getFavoriteEvents(it)
        }

        favoriteViewModel.model.observe(viewLifecycleOwner, ::updateUi)

        return binding.root
    }

    private fun updateUi(model: UiModel){
        binding.progressBar.visibility = if (model == Loading) View.VISIBLE else View.GONE
        when (model){
            is Content -> {
                if (model.events.isNotEmpty()){
                    favoriteAdapter.submitList(model.events)
                    binding.emptyState.visibility = View.GONE
                    binding.favoritesRecycler.visibility = View.VISIBLE
                }else{
                    binding.emptyState.visibility = View.VISIBLE
                    binding.favoritesRecycler.visibility = View.GONE
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item, NavHostFragment.findNavController(this)) ||
                super.onOptionsItemSelected(item)
    }

    companion object {
        private const val TAG = "FavouriteFragment"
    }
}
