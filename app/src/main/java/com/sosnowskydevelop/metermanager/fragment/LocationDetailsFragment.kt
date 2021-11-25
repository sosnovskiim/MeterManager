package com.sosnowskydevelop.metermanager.fragment

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.sosnowskydevelop.metermanager.LocationViewModel
import com.sosnowskydevelop.metermanager.LocationViewModelFactory
import com.sosnowskydevelop.metermanager.MetersApplication
import com.sosnowskydevelop.metermanager.R
import com.sosnowskydevelop.metermanager.data.Location
import com.sosnowskydevelop.metermanager.databinding.LocationDetailsFragmentBinding

class LocationDetailsFragment : Fragment() {
    private lateinit var binding: LocationDetailsFragmentBinding

    private val locationViewModel: LocationViewModel by viewModels {
        LocationViewModelFactory((activity?.application as MetersApplication).locationRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LocationDetailsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.locationSaveBtn.setOnClickListener {
            if (TextUtils.isEmpty(binding.locationNameEdittext.text)) {
                // TODO check
            } else {
                val name = binding.locationNameEdittext.text.toString()
                val description = binding.locationDescriptionEdittext.text.toString()
                locationViewModel.insert(Location(0, name, description))
                findNavController().navigate(R.id.action_locationDetailsFragment_to_locationListFragment)
            }
        }

        val args: LocationDetailsFragmentArgs by navArgs()
        val locationId = args.locationId
        if (locationId == 0) {
            (requireActivity() as AppCompatActivity).supportActionBar?.title = resources.getString(R.string.location_new)
        } else {
            (requireActivity() as AppCompatActivity).supportActionBar?.title = resources.getString(R.string.location_edit)
        }

        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}
