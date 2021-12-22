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
import androidx.recyclerview.widget.LinearLayoutManager
import com.sosnowskydevelop.metermanager.MetersApplication
import com.sosnowskydevelop.metermanager.R
import com.sosnowskydevelop.metermanager.adapter.ReadingListAdapter
import com.sosnowskydevelop.metermanager.data.Location
import com.sosnowskydevelop.metermanager.data.Meter
import com.sosnowskydevelop.metermanager.databinding.ReadingListFragmentBinding
import com.sosnowskydevelop.metermanager.viewmodel.MeterViewModel
import com.sosnowskydevelop.metermanager.viewmodel.MeterViewModelFactory
import com.sosnowskydevelop.metermanager.viewmodel.ReadingViewModel
import com.sosnowskydevelop.metermanager.viewmodel.ReadingViewModelFactory

class ReadingListFragment : Fragment() {
    private lateinit var binding: ReadingListFragmentBinding
    private var location: Location? = null
    private var meter: Meter? = null

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
        location = args.location
        meter = args.meter

        (requireActivity() as AppCompatActivity).supportActionBar?.title = meter?.name
        val readingAdapter = meter?.let {ReadingListAdapter(location = location, meter = it)}
        binding.rvReadingList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvReadingList.adapter = readingAdapter
        readingViewModel.getAllReadingsByMeterId(meterId = meter?.id).observe(this, {
            it?.let { readingAdapter?.submitList(it) }
        })

        binding.btnAddNewReading.setOnClickListener {
            findNavController().navigate(
                ReadingListFragmentDirections
                    .actionReadingListFragmentToReadingDetailsFragment(location = location, meter = meter, reading = null))
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
                            + meter?.name
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
                                        location = location
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
                    ReadingListFragmentDirections.actionReadingListFragmentToMeterDetailsFragment(location, meter))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}