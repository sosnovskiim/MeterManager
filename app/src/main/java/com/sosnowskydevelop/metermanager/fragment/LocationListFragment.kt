package com.sosnowskydevelop.metermanager.fragment

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.sosnowskydevelop.metermanager.*
import com.sosnowskydevelop.metermanager.LocationListAdapter
import com.sosnowskydevelop.metermanager.databinding.LocationListFragmentBinding
import com.sosnowskydevelop.metermanager.viewmodel.LocationViewModel
import com.sosnowskydevelop.metermanager.viewmodel.LocationViewModelFactory

class LocationListFragment : Fragment() {
    private lateinit var binding: LocationListFragmentBinding

    private val locationViewModel: LocationViewModel by viewModels {
        LocationViewModelFactory((activity?.application as MetersApplication).locationRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
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

        val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
        actionBar?.title = resources.getString(R.string.locations_title)
        actionBar?.setDisplayHomeAsUpEnabled(false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.location_list_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.location_menu_add -> {
                findNavController().navigate(LocationListFragmentDirections.actionLocationListFragmentToLocationDetailsFragment(0))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
