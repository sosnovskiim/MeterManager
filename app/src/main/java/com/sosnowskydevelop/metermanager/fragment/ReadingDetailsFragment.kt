package com.sosnowskydevelop.metermanager.fragment

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.sosnowskydevelop.metermanager.MetersApplication
import com.sosnowskydevelop.metermanager.R
import com.sosnowskydevelop.metermanager.Unit
import com.sosnowskydevelop.metermanager.data.DateConverter
import com.sosnowskydevelop.metermanager.data.Meter
import com.sosnowskydevelop.metermanager.data.Reading
import com.sosnowskydevelop.metermanager.databinding.ReadingDetailsFragmentBinding
import com.sosnowskydevelop.metermanager.viewmodel.ReadingViewModel
import com.sosnowskydevelop.metermanager.viewmodel.ReadingViewModelFactory
import java.lang.NumberFormatException
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

class ReadingDetailsFragment : Fragment() {

    private lateinit var binding: ReadingDetailsFragmentBinding
    private lateinit var meter: Meter
    private lateinit var reading: Reading
    private var isNew = true
    private lateinit var valueField: EditText
    private lateinit var dateField: EditText
    private var calendar = Calendar.getInstance()
    private var dateIsWrong = false
    private var isLastReading = true

    private val readingViewModel: ReadingViewModel by viewModels {
        ReadingViewModelFactory((activity?.application as MetersApplication).readingRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = ReadingDetailsFragmentBinding.inflate(inflater, container, false)
        valueField = binding.readingValueEdittext
        dateField = binding.readingDateEdittext
        valueField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                valueField.background = resources.getDrawable(R.drawable.edit_text_border) // TODO replace deprecated method
            }
        })
        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, day ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, day)
            val selectedDate = calendar.time
            if (selectedDate > Date()) {
                dateField.background =
                    resources.getDrawable(R.drawable.edit_text_border_err) // TODO replace deprecated method
                dateIsWrong = true
            } else {
                dateIsWrong = false
            }
            dateField.setText(DateConverter.dateToString(selectedDate))
        }
        binding.btnReadingAddDate.setOnClickListener {
            dateField.background = resources.getDrawable(R.drawable.edit_text_border) // TODO replace deprecated method
            DatePickerDialog(
                requireContext(),
                dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: ReadingDetailsFragmentArgs by navArgs()
        meter = args.meter
        val readingId = args.readingId
        isNew = readingId == -1

        if (isNew) {
            (requireActivity() as AppCompatActivity).supportActionBar?.title = resources.getString(R.string.reading_new_title)
            binding.readingDateEdittext.setText(DateConverter.dateToString(Date()))
            binding.isLastReading.isChecked = true
        } else {
            (requireActivity() as AppCompatActivity).supportActionBar?.title = resources.getString(R.string.meter_edit_title)
            readingViewModel.getReadingById(readingId).observe(this, {
                reading = it
                valueField.setText(reading.value.toString())
                dateField.setText(DateConverter.dateToString(reading.date))
            })
            binding.isLastReading.isChecked = false
        }
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.reading_details_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.reading_menu_save -> {
                val newValue: Float
                try {
                    newValue = valueField.text.toString().toFloat()
                } catch(e: NumberFormatException) {
                    readingValueError(R.string.reading_value_empty)
                    return true
                }
                val date = DateConverter.stringToDate(dateField.text.toString())
                if (dateIsWrong) {
                    Toast.makeText(activity, R.string.reading_future_date, Toast.LENGTH_LONG).show()
                } else if (date != null) {
                    if (newValue < meter.lastReadingValue && date >= meter.lastReadingDate) {
                        readingValueError(R.string.last_reading_less_then_new_err)
                    } else {
                        if (isNew) {
                            val newReading = Reading(
                                id = 0,
                                meterId = meter.id,
                                date = date,
                                value = newValue,
                                unit = meter.unit
                            )
                            readingViewModel.insert(newReading)
                            changeLastReading(newReading)
                            closeOk(R.string.reading_added)

                        } else {
                            var isChanged = false
                            if (reading.value != newValue) {
                                reading.value = newValue
                                isChanged = true
                            }
                            if (reading.date != date) {
                                reading.date = date
                                isChanged = true
                            }
                            if (isChanged) {
                                readingViewModel.update(reading)
                                changeLastReading(reading)
                                closeOk(R.string.reading_edited)
                            } else {
                                closeOk(null)
                            }
                        }
                    }
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun readingValueError(messageId: Int) {
        Toast.makeText(activity, getString(messageId), Toast.LENGTH_LONG).show()
        valueField.background = resources.getDrawable(R.drawable.edit_text_border_err) // TODO replace deprecated method
        valueField.requestFocus()
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
            ReadingDetailsFragmentDirections.actionReadingDetailsFragmentToReadingListFragment(
                locationId = meter.locationId,
                meter = meter
            )
        )
    }

    private fun changeLastReading(reading: Reading) {
        if (binding.isLastReading.isChecked) {
            readingViewModel.addLastReading(reading)
            meter.lastReadingDate = reading.date
            meter.lastReadingValue = reading.value
        }
    }
}