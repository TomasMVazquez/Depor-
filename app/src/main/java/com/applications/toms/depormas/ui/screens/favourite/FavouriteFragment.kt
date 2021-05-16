package com.applications.toms.depormas.ui.screens.favourite

import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.applications.toms.depormas.R
import com.applications.toms.depormas.data.database.local.RoomEventDataSource
import com.applications.toms.depormas.data.database.local.RoomFavoriteDataSource
import com.applications.toms.depormas.data.database.local.event.EventDatabase
import com.applications.toms.depormas.data.database.local.favorite.FavoriteDatabase
import com.applications.toms.depormas.data.database.remote.EventFirestoreServer
import com.applications.toms.depormas.data.repository.EventRepository
import com.applications.toms.depormas.data.repository.FavoriteRepository
import com.applications.toms.depormas.databinding.FragmentFavouriteBinding
import com.applications.toms.depormas.domain.Event
import com.applications.toms.depormas.ui.adapters.FavoriteAdapter
import com.applications.toms.depormas.ui.adapters.FavoriteListener
import com.applications.toms.depormas.usecases.GetEvents
import com.applications.toms.depormas.usecases.MyFavorite
import com.applications.toms.depormas.utils.dateStringComparator
import com.applications.toms.depormas.utils.getViewModel
import com.applications.toms.depormas.utils.snackBar
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.scope.ScopeFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavouriteFragment : ScopeFragment() {

    private lateinit var binding: FragmentFavouriteBinding
    private val favoriteViewModel: FavoriteViewModel by viewModel()

    private val favoriteAdapter by lazy {
        FavoriteAdapter(FavoriteListener {
            Toast.makeText(requireContext(), it.event_name,Toast.LENGTH_SHORT).show()
        })
    }

    private lateinit var swipeIcon: Drawable

    private val onSwipeCallback = object: ItemTouchHelper.SimpleCallback(0,
            ItemTouchHelper.LEFT) {
        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean = false

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            favoriteViewModel.onSwipeItemToRemoveFavorite(favoriteAdapter.currentList[viewHolder.adapterPosition])
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
                c.drawColor(ContextCompat.getColor(requireContext(),R.color.redLightColor))

            val iconMargin = (viewHolder.itemView.height - swipeIcon.intrinsicHeight) / 2

            swipeIcon.bounds = Rect(
                    viewHolder.itemView.right - iconMargin - swipeIcon.intrinsicWidth,
                    viewHolder.itemView.top + iconMargin,
                    viewHolder.itemView.right - iconMargin,
                    viewHolder.itemView.bottom - iconMargin
            )

            swipeIcon.draw(c)

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_favourite, container, false)

        setHasOptionsMenu(true)

        swipeIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_remove)!!

        binding.favoritesRecycler.adapter = favoriteAdapter

        ItemTouchHelper(onSwipeCallback).attachToRecyclerView(binding.favoritesRecycler)

        favoriteViewModel.favorites.observe(viewLifecycleOwner,::updateUi)

        favoriteViewModel.onFavoriteRemoved.observe(viewLifecycleOwner){ event ->
            event.getContentIfNotHandled()?.let { mapItemRemoved ->
                when(mapItemRemoved[STATUS]){
                    0 -> Snackbar.make(binding.root, getString(R.string.favorite_remove_msg), Snackbar.LENGTH_LONG)
                            .setAction(getString(R.string.snack_action_undo)){
                                favoriteViewModel.saveFavoriteAfterRemoveIt(mapItemRemoved[FAVORITE] as Event)
                            }.show()
                    1 -> binding.root.snackBar(getString(R.string.favorite_undo_msg))
                }
            }
        }

        return binding.root
    }

    private fun updateUi(list: List<Event>){
        binding.progressBar.visibility = View.GONE
        if (list.isNotEmpty()){
            favoriteAdapter.submitList(list.sortedWith(dateStringComparator))
            binding.emptyState.visibility = View.GONE
            binding.favoritesRecycler.visibility = View.VISIBLE
        }else{
            binding.emptyState.visibility = View.VISIBLE
            binding.favoritesRecycler.visibility = View.GONE
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
        const val STATUS = "status"
        const val FAVORITE = "favorite"
    }
}
