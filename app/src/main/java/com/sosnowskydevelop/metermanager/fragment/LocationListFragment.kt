package com.sosnowskydevelop.metermanager.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.sosnowskydevelop.metermanager.*
import com.sosnowskydevelop.metermanager.LocationListAdapter
import com.sosnowskydevelop.metermanager.databinding.LocationListFragmentBinding

class LocationListFragment : Fragment() {
    private lateinit var binding: LocationListFragmentBinding

    private val locationViewModel: LocationViewModel by viewModels {
        LocationViewModelFactory((activity?.application as MetersApplication).locationRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LocationListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.locationRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        val locationAdapter = LocationListAdapter()
        binding.locationRecyclerview.adapter = locationAdapter

        locationViewModel.allLocations.observe(this, Observer { locations ->
            locations?.let { locationAdapter.submitList(it) }
        })

        binding.locationAddBtn.setOnClickListener {
            findNavController().navigate(R.id.action_locationListFragment_to_locationDetailsFragment)
        }

        binding.locationDeleteBtn.setOnClickListener{
            locationViewModel.deleteAll()
        }
    }
}