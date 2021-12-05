package com.sosnowskydevelop.metermanager.fragment

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.sosnowskydevelop.metermanager.MetersApplication
import com.sosnowskydevelop.metermanager.R
import com.sosnowskydevelop.metermanager.adapter.ReadingListAdapter
import com.sosnowskydevelop.metermanager.data.Meter
import com.sosnowskydevelop.metermanager.databinding.ReadingListFragmentBinding
import com.sosnowskydevelop.metermanager.viewmodel.MeterViewModel
import com.sosnowskydevelop.metermanager.viewmodel.MeterViewModelFactory
import com.sosnowskydevelop.metermanager.viewmodel.ReadingViewModel
import com.sosnowskydevelop.metermanager.viewmodel.ReadingViewModelFactory
import java.util.*

class ReadingListFragment : Fragment() {
    private lateinit var binding: ReadingListFragmentBinding
    private var locationId: Int = 0
    private lateinit var meter: Meter

    private val meterViewModel: MeterViewModel by viewModels {
        MeterViewModelFactory((activity?.application as MetersApplication).meterRepository)
    }

    private val readingViewModel: ReadingViewModel by viewModels {
        ReadingViewModelFactory((activity?.application as MetersApplication).readingRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = ReadingListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: ReadingListFragmentArgs by navArgs()
        locationId = args.locationId
        val meterId = args.meterId

        meterViewModel.getMeterById(meterId = meterId).observe(this, {
            meter = it
            (requireActivity() as AppCompatActivity).supportActionBar?.title = meter.name
        })

        val readingAdapter = ReadingListAdapter()
        binding.rvReadingList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvReadingList.adapter = readingAdapter
        readingViewModel.getAllReadingsByMeterId(meterId =meterId).observe(this, {
            it?.let { readingAdapter.submitList(it) }
        })

        binding.btnAddNewReading.setOnClickListener {
            findNavController().navigate(
                ReadingListFragmentDirections.actionReadingListFragmentToReadingDetailsFragment(meter.id, -1))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.reading_list_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete_meter -> {
                lateinit var dialog: AlertDialog
                val builder = AlertDialog.Builder(this.context)
                builder.setTitle(resources.getString(R.string.meter_delete_dialog_title))
                builder.setMessage(
                    resources.getString(R.string.meter_delete_dialog_message)
                            + ' '
                            + meter.name
                            + '?'
                )
                val dialogClickListener = DialogInterface.OnClickListener { _, which ->
                    when (which) {
                        DialogInterface.BUTTON_POSITIVE -> {
                            meterViewModel.deleteMeter(meter = meter)
                            Toast.makeText(
                                requireActivity(),
                                resources.getString(R.string.meter_delete_toast),
                                Toast.LENGTH_LONG
                            ).show()
                            findNavController().navigate(
                                ReadingListFragmentDirections
                                    .actionReadingListFragmentToMeterListFragment(
                                        locationId = locationId
                                    )
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
            R.id.edit_meter -> {
                findNavController().navigate(
                    ReadingListFragmentDirections.actionReadingListFragmentToMeterDetailsFragment(locationId, meter.id))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}