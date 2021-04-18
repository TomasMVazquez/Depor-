package com.applications.toms.depormas.ui.screens.create.bottomsheets

import android.annotation.SuppressLint
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.applications.toms.depormas.R
import com.applications.toms.depormas.databinding.BottomSheetMapBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.*

class BottomSheetMap(val listener: BottomSheetInterface): BottomSheetDialogFragment(),
    OnMapReadyCallback {

    private lateinit var binding: BottomSheetMapBinding
    private lateinit var map: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.bottom_sheet_map,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap?) {
        map = googleMap!!

        val latitude = 39.5201395441596
        val longitude = -3.3123215747346584
        val spain = LatLng(latitude, longitude)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(spain, 6f))

        map.setOnCameraMoveStartedListener {

        }

        map.setOnCameraIdleListener {

        }

        setMapLongClick(map)
        //TODO CHECK IF LOCATION IS ENABLE
        map.isMyLocationEnabled = true
    }

    private fun setMapLongClick(map: GoogleMap) {
        map.setOnMapLongClickListener { latLng ->
            // A Snippet is Additional text that's displayed below the title.
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

        binding.mapAddressTIET.text = Editable.Factory.getInstance().newEditable(address.getAddressLine(0).toString())
    }

    override fun getTheme(): Int = R.style.BottomSheetMenuTheme
}