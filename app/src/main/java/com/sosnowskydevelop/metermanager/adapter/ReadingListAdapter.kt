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
import com.sosnowskydevelop.metermanager.data.Reading
import com.sosnowskydevelop.metermanager.databinding.ReadingListItemBinding
import com.sosnowskydevelop.metermanager.fragment.ReadingListFragmentDirections

class ReadingListAdapter : ListAdapter<Reading, ReadingListAdapter.ReadingViewHolder>(ReadingListAdapter) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReadingViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ReadingListItemBinding.inflate(layoutInflater)
        return ReadingViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O) // TODO хз чё это
    override fun onBindViewHolder(holder: ReadingViewHolder, position: Int) {
        val currentReading = getItem(position)
        holder.itemView.setOnClickListener {
            holder.itemView.findNavController().navigate(
                ReadingListFragmentDirections
                    .actionReadingListFragmentToReadingDetailsFragmentEdit(
                        meterId = currentReading.meterId,
                        readingId = currentReading.id
                    )
            )
        }
        holder.binding.readingItemValue.text = currentReading.value.toString()
        holder.binding.readingItemDate.text = DateConverter.dateToString(currentReading.date)
    }

    class ReadingViewHolder(val binding: ReadingListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    companion object : DiffUtil.ItemCallback<Reading>() {
        override fun areItemsTheSame(oldItem: Reading, newItem: Reading): Boolean =
            oldItem === newItem

        override fun areContentsTheSame(oldItem: Reading, newItem: Reading): Boolean =
            oldItem.id == newItem.id
    }
}