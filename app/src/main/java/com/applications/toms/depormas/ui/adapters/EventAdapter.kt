package com.applications.toms.depormas.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.applications.toms.depormas.databinding.RecyclerEventItemBinding
import com.applications.toms.depormas.domain.Event
import com.applications.toms.depormas.domain.Location
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class EventAdapter(private val clickListener: EventListener):
    ListAdapter<Event, EventAdapter.ViewHolder>(ClassDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item!!, clickListener)
    }

    class ViewHolder private constructor(val binding: RecyclerEventItemBinding): RecyclerView.ViewHolder(binding.root), OnMapReadyCallback {

        private lateinit var map: GoogleMap
        private lateinit var context: Context
        private lateinit var location: Location

        fun bind(item: Event, clickListener: EventListener) {
            context = itemView.context
            location = item.location

            binding.clickListener = clickListener
            binding.event = item
            val mapView = binding.eventMap
            mapView.onCreate(null)
            mapView.onResume()
            mapView.getMapAsync(this)
            binding.executePendingBindings()
        }

        override fun onMapReady(googleMap: GoogleMap) {
            MapsInitializer.initialize(context)
            map = googleMap
            val position = LatLng(location.latitude,location.longitude)
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 14f))
            map.addMarker(
                MarkerOptions()
                        .position(position)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
            )
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater =  LayoutInflater.from(parent.context)
                val binding = RecyclerEventItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(
                        binding
                )
            }
        }
    }

    class ClassDiffCallback : DiffUtil.ItemCallback<Event>() {
        override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem == newItem
        }

    }
}

class EventListener(val clickListener: (event: Event) -> Unit){
    fun onClick(event: Event) = clickListener(event)
}