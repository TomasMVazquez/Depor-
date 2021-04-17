
package com.applications.toms.depormas.ui.screens.create

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.applications.toms.depormas.R
import com.applications.toms.depormas.domain.Sport
import com.applications.toms.depormas.data.repository.SportRepository
import com.applications.toms.depormas.data.database.remote.Network
import com.applications.toms.depormas.data.database.local.RoomDataSource
import com.applications.toms.depormas.data.database.local.SportDatabase
import com.applications.toms.depormas.databinding.FragmentCreateEventBinding
import com.applications.toms.depormas.ui.adapters.SportAdapter
import com.applications.toms.depormas.ui.adapters.SportListener
import com.applications.toms.depormas.usecases.GetSports
import com.applications.toms.depormas.utils.getViewModel

class CreateEventFragment : Fragment(), BottomSheetInterface {

    private lateinit var binding: FragmentCreateEventBinding
    private lateinit var createViewModel: CreateViewModel

    private val pickDayBottomSheet by lazy { BottomSheetPickDay(this) }
    private val pickTimeBottomSheet by lazy { BottomSheetPickTime(this) }

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

        val sportDatabase = SportDatabase.getInstance(requireContext())
        val sportRepository = SportRepository(lifecycleScope, RoomDataSource(sportDatabase), Network())
        val getSport = GetSports(sportRepository)

        createViewModel = getViewModel { CreateViewModel(getSport) }

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
                //TODO HANDLE CREATE EVENT BTN
            }
        }

        binding.eventDayTIET.setOnFocusChangeListener { _, b ->
            if (b) showDayPickerBottomSheetFragment()
        }

        binding.eventTimeTIET.setOnFocusChangeListener { _, b ->
            if (b) showTimePickerBottomSheetFragment()
        }

        return binding.root
    }

    private fun showDayPickerBottomSheetFragment() {
        pickDayBottomSheet.show(requireActivity().supportFragmentManager, PICK_DAY_TAG)
    }

    private fun showTimePickerBottomSheetFragment(){
        pickTimeBottomSheet.show(requireActivity().supportFragmentManager, PICK_TIME_TAG)
    }

    private fun updateAdapter(sportChecked: Sport) {
        sportAdapter.currentList.map { sport ->
            if (sport.id != sportChecked.id && sport.choosen) sport.choosen = false
        }
        sportChecked.choosen = !sportChecked.choosen
        if (sportChecked.choosen && sportChecked.max_players!! > 0) {
            binding.eventParticipantsTIET.text = Editable.Factory.getInstance().newEditable(sportChecked.max_players.toString())
        }else {
            binding.eventParticipantsTIET.text = Editable.Factory.getInstance().newEditable("")
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

    override fun getDataFromBottomSheet(code: Int, data: String) {
        when(code){
            PICK_DAY_CODE -> {
                pickDayBottomSheet.dismiss()
                binding.eventDayTIET.apply {
                    setText(data)
                    clearFocus()
                }
            }

            PICK_TIME_CODE -> {
                pickTimeBottomSheet.dismiss()
                binding.eventTimeTIET.apply {
                    setText(data)
                    clearFocus()
                }
            }
        }
    }

    companion object {
        private const val TAG = "CreateEventFragment"
        private const val PICK_DAY_TAG = "BottomSheetPickDay"
        private const val PICK_TIME_TAG = "BottomSheetPickTime"
        const val PICK_DAY_CODE = 0
        const val PICK_TIME_CODE = 1
    }
}
