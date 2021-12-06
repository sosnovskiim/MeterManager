package com.sosnowskydevelop.metermanager.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.*
import android.widget.EditText
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.sosnowskydevelop.metermanager.viewmodel.LocationViewModel
import com.sosnowskydevelop.metermanager.viewmodel.LocationViewModelFactory
import com.sosnowskydevelop.metermanager.MetersApplication
import com.sosnowskydevelop.metermanager.R
import com.sosnowskydevelop.metermanager.data.Location
import com.sosnowskydevelop.metermanager.databinding.LocationDetailsFragmentBinding

class LocationDetailsFragment : Fragment() {

    private lateinit var binding: LocationDetailsFragmentBinding
    private lateinit var editTextName: EditText
    private var isNew = true
    private var isChanged = false
    private lateinit var location: Location

    private val locationViewModel: LocationViewModel by viewModels {
        LocationViewModelFactory((activity?.application as MetersApplication).locationRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = LocationDetailsFragmentBinding.inflate(inflater, container, false)
        binding.locationNameEdittext.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                binding.locationNameEdittext.background = ContextCompat.getDrawable(requireContext(), R.drawable.edit_text_border)
            }
        })
        editTextName = binding.locationNameEdittext
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: LocationDetailsFragmentArgs by navArgs()
        val locationId = args.locationId
        isNew = locationId == 0
        if (isNew) {
            (requireActivity() as AppCompatActivity).supportActionBar?.title = resources.getString(R.string.location_new_title)
        } else {
            (requireActivity() as AppCompatActivity).supportActionBar?.title = resources.getString(R.string.location_edit_title)
            locationViewModel.getLocationById(locationId).observe(this, {
                location = it
                binding.locationNameEdittext.setText(it.name)
                binding.locationDescriptionEdittext.setText(it.description)
            })
        }

        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.location_details_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.location_menu_save -> {
                if (TextUtils.isEmpty(editTextName.text)) {
                    locationNameError(R.string.input_location_name_empty_err)
                } else {
                    val description = binding.locationDescriptionEdittext.text.toString()
                    if (isNew) {
                        if (!locationViewModel.isLocationNameUnique(editTextName.text.toString(), 0)) {
                            locationNameError(R.string.input_location_name_duplicate_err)
                        } else {
                            locationViewModel.insert(Location(0, editTextName.text.toString(), description))
                            isChanged = true
                            closeOk(R.string.location_added)
                        }
                    } else {
                        if (!locationViewModel.isLocationNameUnique(editTextName.text.toString(), location.id)
                        ) {
                            locationNameError(R.string.input_location_name_duplicate_err)
                        } else {
                            if (location.name != editTextName.text.toString()) {
                                location.name = editTextName.text.toString()
                                isChanged = true
                            }
                            if (location.description != description) {
                                location.description = description
                                isChanged = true
                            }
                            locationViewModel.update(location)
                            closeOk(R.string.location_edited)
                        }
                    }
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun locationNameError(messageId: Int) {
        Toast.makeText(activity, getString(messageId), Toast.LENGTH_LONG).show()
        editTextName.background = ContextCompat.getDrawable(requireContext(), R.drawable.edit_text_border_err)
        editTextName.requestFocus()
    }

    private fun closeOk(messageId: Int) {
        if (isChanged) {Toast.makeText(activity, getString(messageId), Toast.LENGTH_LONG).show()}
        findNavController().navigate(
            LocationDetailsFragmentDirections.actionLocationDetailsFragmentToLocationListFragment())
    }
}
