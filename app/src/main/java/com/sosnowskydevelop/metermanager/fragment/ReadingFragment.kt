package com.sosnowskydevelop.metermanager.fragment

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.sosnowskydevelop.metermanager.MetersApplication
import com.sosnowskydevelop.metermanager.R
import com.sosnowskydevelop.metermanager.data.DateConverter
import com.sosnowskydevelop.metermanager.data.Meter
import com.sosnowskydevelop.metermanager.data.Reading
import com.sosnowskydevelop.metermanager.databinding.ReadingFragmentBinding
import com.sosnowskydevelop.metermanager.viewmodel.ReadingViewModel
import com.sosnowskydevelop.metermanager.viewmodel.ReadingViewModelFactory

class ReadingFragment : Fragment() {
    private lateinit var binding: ReadingFragmentBinding
    private lateinit var reading: Reading
    private lateinit var meter: Meter

    private val readingViewModel: ReadingViewModel by viewModels {
        ReadingViewModelFactory((activity?.application as MetersApplication).readingRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = ReadingFragmentBinding.inflate(inflater, container, false)
        binding.readingDateLabel.text = resources.getString(R.string.date_label)
        binding.readingValueLabel.text = resources.getString(R.string.value_label)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: ReadingFragmentArgs by navArgs()
        meter = args.meter
        (requireActivity() as AppCompatActivity).supportActionBar?.title = meter.name
        readingViewModel.getReadingById(args.readingId).observe(this, {
            reading = it
            binding.readingDateValue.text = DateConverter.dateToString(it.date)
            binding.readingValueValue.text = it.value.toString()
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.reading_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete_reading -> {
                lateinit var dialog: AlertDialog
                val builder = AlertDialog.Builder(this.context)
                builder.setTitle(resources.getString(R.string.meter_delete_dialog_title))
                builder.setMessage(resources.getString(R.string.reading_delete_dialog_message))
                val dialogClickListener = DialogInterface.OnClickListener { _, which ->
                    when (which) {
                        DialogInterface.BUTTON_POSITIVE -> {
                            readingViewModel.delete(reading = reading)
                            Toast.makeText(
                                requireActivity(),
                                resources.getString(R.string.reading_delete_toast),
                                Toast.LENGTH_LONG
                            ).show()
                            findNavController().navigate(
                                ReadingFragmentDirections
                                    .actionReadingFragmentToReadingListFragment(
                                        locationId = meter.locationId,
                                        meter = meter)
                            )
                        }
                        DialogInterface.BUTTON_NEGATIVE -> dialog.dismiss()
                    }
                }
                builder.setPositiveButton(
                    resources.getString(R.string.meter_delete_dialog_positive),
                    dialogClickListener
                )
                builder.setNegativeButton(
                    resources.getString(R.string.meter_delete_dialog_negative),
                    dialogClickListener
                )
                dialog = builder.create()
                dialog.show()
                true
            }
            R.id.edit_reading -> {
                findNavController().navigate(
                    ReadingFragmentDirections
                        .actionReadingFragmentToReadingDetailsFragment(
                            meter = meter,
                            readingId = reading.id)
                )
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}