package com.applications.toms.depormas.ui.screens.create

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.applications.toms.depormas.R
import com.applications.toms.depormas.data.model.Sport
import com.applications.toms.depormas.data.repository.SportRepository
import com.applications.toms.depormas.data.source.Network
import com.applications.toms.depormas.data.source.database.RoomDataSource
import com.applications.toms.depormas.databinding.FragmentCreateEventBinding
import com.applications.toms.depormas.ui.adapters.SportAdapter
import com.applications.toms.depormas.ui.adapters.SportListener
import com.applications.toms.depormas.utils.getViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class CreateEventFragment : Fragment() {

    private lateinit var binding: FragmentCreateEventBinding
    private lateinit var createViewModel: CreateViewModel

    private val sportAdapter by lazy { SportAdapter(SportListener { sport ->
        updateAdapter(sport)
    }) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_create_event, container, false)

        setHasOptionsMenu(true)

        val sportRepository = SportRepository(lifecycleScope, RoomDataSource(requireContext()), Network())

        createViewModel = getViewModel { CreateViewModel(sportRepository) }

        binding.createViewModel = createViewModel

        binding.sportRecycler.adapter = sportAdapter

        createViewModel.sports.observe(viewLifecycleOwner){
            sportAdapter.submitList(it)
        }

        createViewModel.cancel.observe(viewLifecycleOwner){
            it.getContentIfNotHandled()?.let {
                findNavController().popBackStack()
            }
        }

        createViewModel.createEvent.observe(viewLifecycleOwner){
            it.getContentIfNotHandled()?.let {
            }
        }

        return binding.root
    }

    private fun updateAdapter(sportChecked: Sport) {
        sportAdapter.currentList.map { sport ->
            if (sport.id != sportChecked.id && sport.choosen) sport.choosen = false
        }
        sportChecked.choosen = !sportChecked.choosen
        if (sportChecked.choosen && sportChecked.max_players!! > 0) {
            binding.eventParticipantsTIET.setText(sportChecked.max_players.toString())
        }else {
            binding.eventParticipantsTIET.setText("")
        }
        sportAdapter.notifyDataSetChanged()
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
        private const val TAG = "CreateEventFragment"
    }
}
