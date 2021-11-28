package com.sosnowskydevelop.metermanager.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.*
import android.widget.EditText
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
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
    private lateinit var name: EditText
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
                binding.locationNameEdittext.background = resources.getDrawable(R.drawable.edit_text_border) // TODO replace deprecated method
            }
        })
        name = binding.locationNameEdittext
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: LocationDetailsFragmentArgs by navArgs()
        val locationId = args.locationId
        if (locationId == 0) {
            (requireActivity() as AppCompatActivity).supportActionBar?.title = resources.getString(R.string.location_new_title)
        } else {
            (requireActivity() as AppCompatActivity).supportActionBar?.title = resources.getString(R.string.location_edit_title)
        }

        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.location_details_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.location_menu_save -> {
                if (TextUtils.isEmpty(name.text)) {
                    locationNameError(R.string.input_location_name_empty_err)
                } else if (!locationViewModel.isLocationNameUnique(name.text.toString())) {
                    locationNameError(R.string.input_location_name_duplicate_err)
                }
                else {
                    val description = binding.locationDescriptionEdittext.text.toString()
                    locationViewModel.insert(Location(0, name.text.toString(), description))
                    findNavController().navigate(
                        LocationDetailsFragmentDirections.actionLocationDetailsFragmentToLocationListFragment())
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun locationNameError(messageId: Int) {
        Toast.makeText(activity, getString(messageId), Toast.LENGTH_LONG).show()
        name.background = resources.getDrawable(R.drawable.edit_text_border_err) // TODO replace deprecated method
        name.requestFocus()
    }
}
