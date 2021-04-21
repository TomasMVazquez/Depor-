package com.applications.toms.depormas.ui.screens.create.bottomsheets

import android.annotation.SuppressLint
import android.app.Dialog
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import com.applications.toms.depormas.R
import com.applications.toms.depormas.data.AndroidPermissionChecker
import com.applications.toms.depormas.data.repository.LocationRepository
import com.applications.toms.depormas.data.source.PermissionChecker.Permission
import com.applications.toms.depormas.databinding.BottomSheetMapBinding
import com.applications.toms.depormas.domain.Location
import com.applications.toms.depormas.ui.screens.create.CreateEventFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.*
import java.util.*

class BottomSheetMap(
        private val coroutineScope: CoroutineScope,
        private val locationRepository: LocationRepository,
        val listener: BottomSheetMapInterface)
    : BottomSheetDialogFragment(), OnMapReadyCallback {

    private lateinit var permissionChecker: AndroidPermissionChecker

    private lateinit var binding: BottomSheetMapBinding
    private lateinit var map: GoogleMap

    private lateinit var behavior: BottomSheetBehavior<*>

    private lateinit var locationChosen: Location

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        bottomSheetDialog.setOnShowListener {
            val bottomSheet = bottomSheetDialog
                    .findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)

            if (bottomSheet != null) {
                behavior = BottomSheetBehavior.from(bottomSheet)
                behavior.halfExpandedRatio = 0.8F
                behavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
                behavior.isDraggable = false
            }
        }
        return bottomSheetDialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.bottom_sheet_map,container,false)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        permissionChecker = AndroidPermissionChecker(requireContext())

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.locationPickerBtn.setOnClickListener {
            if (this::locationChosen.isInitialized)
                listener.getDataFromMapBottomSheet(CreateEventFragment.PICK_MAP_CODE,locationChosen)
        }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        map = googleMap!!
        enableMyLocation()
        setMapLongClick()
    }

    private fun setLocation() {
        coroutineScope.launch {
            val findLastLocation = locationRepository.findLastLocation()
            val latitude = findLastLocation["latitude"]!!
            val longitude = findLastLocation["longitude"]!!
            val location = LatLng(latitude, longitude)
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, if(map.isMyLocationEnabled) 14F else 1F))
        }
    }

    @SuppressLint("MissingPermission")
    private fun enableMyLocation() {
        if (permissionChecker.check(Permission.FINE_LOCATION)) {
            map.isMyLocationEnabled = true
        }
        setLocation()
    }

    private fun setMapLongClick() {
        map.setOnMapLongClickListener { latLng ->
            try {
                val geoCoder = Geocoder(requireContext(), Locale.getDefault())
                val addresses = geoCoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
                addMarker(addresses[0])
            }catch (e: Exception){
                Log.d("MAP", "setMapLongClick: ${e.message}")
            }
        }
    }

    private fun addMarker(address: Address) {
        map.clear()
        val title = getString(R.string.dropped_pin)
        val snippet = address.getAddressLine(0).toString()

        map.addMarker(
            MarkerOptions()
                .position(LatLng(address.latitude, address.longitude))
                .title(title)
                .snippet(snippet)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
        )

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(address.latitude, address.longitude), 15f))

        locationChosen = Location(
                id = null,
                name = "${address.thoroughfare}, ${address.subThoroughfare}",
                address = address.getAddressLine(0),
                latitude = address.latitude,
                longitude = address.longitude,
                city = address.adminArea,
                countryCode = address.countryCode
        )
        binding.mapAddressTIET.text = Editable.Factory.getInstance().newEditable(locationChosen.address)
    }

    override fun getTheme(): Int = R.style.BottomSheetMenuTheme
}