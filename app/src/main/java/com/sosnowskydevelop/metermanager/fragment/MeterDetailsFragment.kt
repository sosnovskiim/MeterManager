package com.sosnowskydevelop.metermanager.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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
import com.sosnowskydevelop.metermanager.data.Location
import com.sosnowskydevelop.metermanager.databinding.MeterDetailsFragmentBinding
import com.sosnowskydevelop.metermanager.viewmodel.LocationViewModel
import com.sosnowskydevelop.metermanager.viewmodel.LocationViewModelFactory

class MeterDetailsFragment : Fragment() {

    private lateinit var binding: MeterDetailsFragmentBinding
    private lateinit var name: EditText
    private var locationId = 0
    private lateinit var meter: Meter
    private var isNew = true
    private var isChanged = false

    private val meterViewModel: MeterViewModel by viewModels {
        MeterViewModelFactory((activity?.application as MetersApplication).meterRepository)
    }

    private val locationViewModel: LocationViewModel by viewModels {
        LocationViewModelFactory((activity?.application as MetersApplication).locationRepository)
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
                binding.meterNameEdittext.background = ContextCompat.getDrawable(requireContext(), R.drawable.edit_text_border)
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
        isNew = meterId == 0
        if (isNew) {
            (requireActivity() as AppCompatActivity).supportActionBar?.title = resources.getString(R.string.meter_new_title)
        } else {
            (requireActivity() as AppCompatActivity).supportActionBar?.title = resources.getString(R.string.meter_edit_title)
            meterViewModel.getMeterById(meterId).observe(this, {
                meter = it
                binding.meterNameEdittext.setText(it.name)
                if (it.unit == Unit.KILOVAT) {
                    binding.rbKv.isChecked = true
                } else {
                    binding.rbM3.isChecked = true
                }
            })
        }

        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setFocusAndShowKeyboard(view = name)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.meter_details_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.meter_menu_save -> {
                if (TextUtils.isEmpty(name.text)) {
                    meterNameError(R.string.input_meter_name_empty_err)
                } else {
                    val unit = if (binding.rbKv.isChecked) {
                        Unit.KILOVAT
                    } else {
                        Unit.METERS3
                    }

                    if (isNew) {
                        if (!binding.rbKv.isChecked && !binding.rbM3.isChecked) {
                            Toast.makeText(activity, getString(R.string.input_meter_reading_err), Toast.LENGTH_LONG).show()
                            binding.rgReadingUnits.background = ContextCompat.getDrawable(requireContext(), R.drawable.edit_text_border_err)
                        } else {
                            meterViewModel.isMeterDuplicate(
                                meterId = 0,
                                locationId = locationId,
                                name = name.text.toString(),
                            ).observe(this, Observer { isMeterDuplicate ->
                                if (isMeterDuplicate == "1") {
                                    meterNameError(R.string.input_meter_name_duplicate_err)
                                } else {
                                    meterViewModel.insert(
                                        Meter(
                                            id = 0,
                                            locationId = locationId,
                                            name = name.text.toString(),
                                            unit = unit
                                        )
                                    )

                                    closeOk(messageId = R.string.meter_added)
                                }
                            })
                        }
                    } else {
                        if (meter.name != name.text.toString()) {
                            meter.name = name.text.toString()
                            isChanged = true
                        }
                        if (meter.unit != unit) {
                            meter.unit = unit
                            isChanged = true
                        }

                        if (isChanged) {
                            meterViewModel.isMeterDuplicate(
                                meterId = meter.id,
                                locationId = locationId,
                                name = name.text.toString(),
                            ).observe(this, Observer { isMeterDuplicate ->
                                if (isMeterDuplicate == "1") {
                                    meterNameError(R.string.input_meter_name_duplicate_err)
                                } else {
                                    meterViewModel.update(meter)

                                    closeOk(messageId = R.string.meter_edited)
                                }
                            })
                        } else {
                            closeOk(messageId = null)
                        }
                    }
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun meterNameError(messageId: Int) {
        Toast.makeText(activity, getString(messageId), Toast.LENGTH_LONG).show()
        name.background = ContextCompat.getDrawable(requireContext(), R.drawable.edit_text_border_err)

        setFocusAndShowKeyboard(view = name)
    }

    private fun setFocusAndShowKeyboard(view: View) {
        view.requestFocus()

        val inputMethodManager: InputMethodManager? =
            ContextCompat.getSystemService(requireContext(), InputMethodManager::class.java)

        inputMethodManager?.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun closeOk(messageId: Int?) {
        if (messageId != null) {
            Toast.makeText(
                activity,
                getString(messageId),
                Toast.LENGTH_LONG,
            ).show()
        }

        findNavController().navigate(
            MeterDetailsFragmentDirections
                .actionMeterDetailsFragmentToMeterListFragment(locationId)
        )
    }
}
