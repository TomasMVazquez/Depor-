
package com.applications.toms.depormas.ui.screens.create

import android.Manifest.permission.*
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
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
import com.applications.toms.depormas.databinding.FragmentCreateEventBinding
import com.applications.toms.depormas.domain.Location
import com.applications.toms.depormas.ui.adapters.SportAdapter
import com.applications.toms.depormas.ui.adapters.SportListener
import com.applications.toms.depormas.ui.screens.create.bottomsheets.*
import com.applications.toms.depormas.usecases.GetSports
import com.applications.toms.depormas.usecases.SaveEvent
import com.applications.toms.depormas.utils.getViewModel
import com.applications.toms.depormas.utils.snackBar

class CreateEventFragment : Fragment(), BottomSheetInterface,BottomSheetMapInterface {

    private lateinit var binding: FragmentCreateEventBinding
    private lateinit var createViewModel: CreateViewModel

    private val pickDayBottomSheet by lazy { BottomSheetPickDay(this) }
    private val pickTimeBottomSheet by lazy { BottomSheetPickTime(this) }
    private val mapBottomSheet by lazy {
        BottomSheetMap(
            lifecycleScope,
            LocationRepository(PlayServicesLocationDataSource(requireContext()),AndroidPermissionChecker(requireContext())),
            this
        )
    }

    private val sportAdapter by lazy { SportAdapter(SportListener { sport ->
        updateAdapter(sport)
    }) }

    private val fineLocationPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()){ granted ->
        when{
            granted -> showMapBottomSheetFragment()
            shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION) -> {
                showMapBottomSheetFragment()
                Log.d(TAG, "rational") //TODO rational
            }
            else -> {
                showMapBottomSheetFragment()
                Log.d(TAG, "denied")//TODO filter depending on permission granted/denied
            }
        }

    }

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

        val getSport = GetSports(
            SportRepository(
                    lifecycleScope,
                    RoomSportDataSource(SportDatabase.getInstance(requireContext())),
                    SportFirestoreServer()
            )
        )
        val saveEvent = SaveEvent(
            EventRepository(
                    lifecycleScope,
                    RoomEventDataSource(EventDatabase.getInstance(requireContext())),
                    EventFirestoreServer()
            )
        )

        createViewModel = getViewModel { CreateViewModel(getSport, saveEvent) }

        binding.createViewModel = createViewModel

        binding.sportRecycler.adapter = sportAdapter

        createViewModel.sports.observe(viewLifecycleOwner){
            sportAdapter.submitList(it)
        }

        // On Focus Events
        binding.eventDayTIET.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) showDayPickerBottomSheetFragment()
        }

        binding.eventTimeTIET.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) showTimePickerBottomSheetFragment()
        }

        binding.eventAddressTIET.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) fineLocationPermission.launch(ACCESS_FINE_LOCATION)
        }

        //Click Events
        createViewModel.cancel.observe(viewLifecycleOwner){
            it.getContentIfNotHandled()?.let {
                findNavController().popBackStack()
            }
        }

        createViewModel.createEvent.observe(viewLifecycleOwner){
            it.getContentIfNotHandled()?.let {
                createViewModel.onEventValidation(
                        binding.eventNameTIET.text.toString(),
                        binding.eventDayTIET.text.toString(),
                        binding.eventTimeTIET.text.toString(),
                        binding.eventParticipantsTIET.text.toString()
                )
            }
        }

        // Results
        createViewModel.createValidation.observe(viewLifecycleOwner){ event ->
            event.getContentIfNotHandled()?.let {
                when(it){
                    -1 -> findNavController().popBackStack()
                    0 -> binding.root.snackBar("No Sport selected")
                    1 -> binding.root.snackBar("No Name selected")
                    2 -> binding.root.snackBar("No Day selected")
                    3 -> binding.root.snackBar("No Time selected")
                    4 -> binding.root.snackBar("No Location selected")
                }
            }
        }

        return binding.root
    }

    private fun showDayPickerBottomSheetFragment() {
        pickDayBottomSheet.show(requireActivity().supportFragmentManager, PICK_DAY_TAG)
    }

    private fun showTimePickerBottomSheetFragment(){
        pickTimeBottomSheet.show(requireActivity().supportFragmentManager, PICK_TIME_TAG)
    }

    private fun showMapBottomSheetFragment(){
        mapBottomSheet.show(requireActivity().supportFragmentManager,MAP_TAG)
    }

    private fun updateAdapter(sportChecked: Sport) {
        sportAdapter.currentList.map { sport ->
            if (sport.id != sportChecked.id && sport.choosen) sport.choosen = false
        }
        sportChecked.choosen = !sportChecked.choosen
        if (sportChecked.choosen) {
            createViewModel.setEventSport(sportChecked)
            if (sportChecked.max_players!! > 0) {
                binding.eventParticipantsTIET.text = Editable.Factory.getInstance().newEditable(sportChecked.max_players.toString())
            } else {
                binding.eventParticipantsTIET.text = Editable.Factory.getInstance().newEditable("")
            }
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

    override fun getDataFromMapBottomSheet(code: Int, data: Location) {
        when(code){
            PICK_MAP_CODE -> {
                mapBottomSheet.dismiss()
                createViewModel.setEventLocation(data)
                binding.eventAddressTIET.apply {
                    setText(data.address)
                    clearFocus()
                }
            }
        }
    }

    companion object {
        private const val TAG = "CreateEventFragment"
        private const val PICK_DAY_TAG = "BottomSheetPickDay"
        private const val PICK_TIME_TAG = "BottomSheetPickTime"
        private const val MAP_TAG = "BottomSheetMap"
        const val PICK_DAY_CODE = 0
        const val PICK_TIME_CODE = 1
        const val PICK_MAP_CODE = 2
    }

}
