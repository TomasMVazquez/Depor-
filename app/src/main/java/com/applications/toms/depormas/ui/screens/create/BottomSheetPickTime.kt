package com.applications.toms.depormas.ui.screens.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.applications.toms.depormas.R
import com.applications.toms.depormas.databinding.BottomSheetPickTimeBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class BottomSheetPickTime(val listener: BottomSheetInterface): BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetPickTimeBinding

    private lateinit var timeChosen: String
    private val formatter = DateTimeFormatter.ofPattern("HH:mm")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(layoutInflater,R.layout.bottom_sheet_pick_time,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.timePicker.setIs24HourView(true)

        timeChosen = LocalTime.now().format(formatter)

        binding.timePicker.setOnTimeChangedListener { _, hour, minute ->
            timeChosen = LocalTime.of(hour,minute).format(formatter)
        }

        binding.timePickerBtn.setOnClickListener {
            listener.getDataFromBottomSheet(CreateEventFragment.PICK_TIME_CODE,timeChosen)
        }
    }

    override fun getTheme(): Int = R.style.BottomSheetMenuTheme

    companion object {
        private const val TAG = "BottomSheetPickTime"
    }
}