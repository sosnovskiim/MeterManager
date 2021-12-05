package com.sosnowskydevelop.metermanager.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sosnowskydevelop.metermanager.data.DateConverter
import com.sosnowskydevelop.metermanager.data.Meter
import com.sosnowskydevelop.metermanager.databinding.MeterListItemBinding
import com.sosnowskydevelop.metermanager.fragment.MeterListFragmentDirections

class MeterListAdapter : ListAdapter<Meter, MeterListAdapter.MeterViewHolder>(MeterListAdapter) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeterViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = MeterListItemBinding.inflate(layoutInflater, parent, false)
        return MeterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MeterViewHolder, position: Int) {
        val currentMeter = getItem(position)
        holder.itemView.setOnClickListener {
            holder.itemView.findNavController().navigate(
                MeterListFragmentDirections
                    .actionMeterListFragmentToReadingListFragment(
                        locationId = currentMeter.locationId,
                        meter = currentMeter
                    )
            )
        }
        holder.binding.meterName.text = currentMeter.name
        holder.binding.tvLastReadingInMeterItem.text = DateConverter.dateToString(currentMeter.lastReadingDate)
    }

    class MeterViewHolder(val binding: MeterListItemBinding) : RecyclerView.ViewHolder(binding.root)

    companion object : DiffUtil.ItemCallback<Meter>() {
        override fun areItemsTheSame(oldItem: Meter, newItem: Meter): Boolean =
            oldItem === newItem

        override fun areContentsTheSame(oldItem: Meter, newItem: Meter): Boolean =
            oldItem.id == newItem.id
    }
}