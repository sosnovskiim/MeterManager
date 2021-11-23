package com.sosnowskydevelop.metermanager

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ListAdapter
import com.sosnowskydevelop.metermanager.LocationListAdapter.LocationViewHolder
import com.sosnowskydevelop.metermanager.data.Location
import com.sosnowskydevelop.metermanager.databinding.LocationListItemBinding
import com.sosnowskydevelop.metermanager.fragment.LocationListFragmentDirections

class LocationListAdapter : ListAdapter<Location, LocationViewHolder>(Companion) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = LocationListItemBinding.inflate(layoutInflater)
        return LocationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        val currentLocation = getItem(position)
        holder.itemView.setOnClickListener {
            holder.itemView.findNavController().navigate(
                LocationListFragmentDirections.actionLocationListFragmentToMeterListFragment(currentLocation.id))
        }
        holder.binding.locationName.text = currentLocation.name
        holder.binding.locationDescription.text = currentLocation.description
    }

    class LocationViewHolder(val binding: LocationListItemBinding) : RecyclerView.ViewHolder(binding.root)

    companion object: DiffUtil.ItemCallback<Location>() {
        override fun areItemsTheSame(oldItem: Location, newItem: Location): Boolean = oldItem === newItem
        override fun areContentsTheSame(oldItem: Location, newItem: Location): Boolean = oldItem.id == newItem.id
    }
}