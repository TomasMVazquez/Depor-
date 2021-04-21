package com.applications.toms.depormas.ui.screens.create.bottomsheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.applications.toms.depormas.R
import com.applications.toms.depormas.databinding.BottomSheetPickDayBinding
import com.applications.toms.depormas.ui.screens.create.CreateEventFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class BottomSheetPickDay(val listener: BottomSheetInterface): BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetPickDayBinding

    private lateinit var dayChosen: String
    private val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(layoutInflater,R.layout.bottom_sheet_pick_day,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.calendar.minDate = Calendar.getInstance().time.time

        val now = LocalDate.now()
        dayChosen = now.format(formatter)

        binding.calendar.setOnDateChangeListener { calendarView, year, month, day ->
            dayChosen = LocalDate.of(year, month, day).format(formatter)
            listener.getDataFromBottomSheet(CreateEventFragment.PICK_DAY_CODE,dayChosen)
        }
    }

    override fun getTheme(): Int = R.style.BottomSheetMenuTheme

    companion object {
        private const val TAG = "BottomSheetPickDay"
    }
}