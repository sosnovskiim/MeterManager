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
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.sosnowskydevelop.metermanager.viewmodel.MeterViewModel
import com.sosnowskydevelop.metermanager.viewmodel.MeterViewModelFactory
import com.sosnowskydevelop.metermanager.MetersApplication
import com.sosnowskydevelop.metermanager.R
import com.sosnowskydevelop.metermanager.data.Meter
import com.sosnowskydevelop.metermanager.Unit
import com.sosnowskydevelop.metermanager.databinding.MeterDetailsFragmentBinding
import com.sosnowskydevelop.metermanager.viewmodel.LocationViewModel
import com.sosnowskydevelop.metermanager.viewmodel.LocationViewModelFactory
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi

class MeterDetailsFragment : Fragment() {

    private lateinit var binding: MeterDetailsFragmentBinding
    private lateinit var name: EditText
    private var locationId = 0

    private val meterViewModel: MeterViewModel by viewModels {
        MeterViewModelFactory((activity?.application as MetersApplication).meterRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = MeterDetailsFragmentBinding.inflate(inflater, container, false)
        binding.meterNameEdittext.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                binding.meterNameEdittext.background = resources.getDrawable(R.drawable.edit_text_border) // TODO replace deprecated method
            }
        })

        binding.rgReadingUnits.setOnCheckedChangeListener { group, _ ->
            group.background = null
        }

        name = binding.meterNameEdittext
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: MeterDetailsFragmentArgs by navArgs()
        locationId = args.locationId
        val meterId = args.meterId
        if (meterId == 0) {
            (requireActivity() as AppCompatActivity).supportActionBar?.title = resources.getString(R.string.meter_new_title)
        } else {
            (requireActivity() as AppCompatActivity).supportActionBar?.title = resources.getString(R.string.meter_edit_title)
        }

        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.meter_details_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.meter_menu_save -> {
                if (TextUtils.isEmpty(name.text)) {
                    meterNameError(R.string.input_meter_name_empty_err)
                } else if (!isMeterNameUnique(locationId, name.text.toString())) {
                    meterNameError(R.string.input_meter_name_duplicate_err)
                } else if (!binding.rbKv.isChecked && !binding.rbM3.isChecked) {
                    Toast.makeText(activity, getString(R.string.input_meter_reading_err), Toast.LENGTH_LONG).show()
                    binding.rgReadingUnits.background = resources.getDrawable(R.drawable.edit_text_border_err) // TODO replace deprecated method
                }
                else {
                    val unit = if (binding.rbKv.isChecked) {
                        Unit.KILOVAT
                    } else {
                        Unit.METERS3
                    }
                    meterViewModel.insert(Meter(id = 0, locationId = locationId, name = name.text.toString(), unit))
                    Toast.makeText(activity, getString(R.string.meter_added), Toast.LENGTH_LONG).show()
                    findNavController().navigate(
                        MeterDetailsFragmentDirections.actionMeterDetailsFragmentToMeterListFragment(locationId))
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun meterNameError(messageId: Int) {
        Toast.makeText(activity, getString(messageId), Toast.LENGTH_LONG).show()
        name.background = resources.getDrawable(R.drawable.edit_text_border_err) // TODO replace deprecated method
        name.requestFocus()
    }

    // TODO not work!!!
    @DelicateCoroutinesApi
    @ExperimentalCoroutinesApi
    private fun isMeterNameUnique(locationId: Int, name: String): Boolean {
        meterViewModel.getAllMetersByLocationId(locationId = locationId).forEach { meter: Meter ->
            if (meter.name == name) {
                return false
            }
        }
        return true
    }
}