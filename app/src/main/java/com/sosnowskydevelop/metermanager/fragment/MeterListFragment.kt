package com.sosnowskydevelop.metermanager.fragment

import android.app.AlertDialog
import android.content.DialogInterface
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
import com.sosnowskydevelop.metermanager.adapter.MeterListAdapter
import com.sosnowskydevelop.metermanager.viewmodel.MeterViewModel
import com.sosnowskydevelop.metermanager.viewmodel.MeterViewModelFactory
import com.sosnowskydevelop.metermanager.viewmodel.LocationViewModel
import com.sosnowskydevelop.metermanager.viewmodel.LocationViewModelFactory
import com.sosnowskydevelop.metermanager.MetersApplication
import com.sosnowskydevelop.metermanager.data.Location
import com.sosnowskydevelop.metermanager.databinding.MeterListFragmentBinding

class MeterListFragment : Fragment() {

    private lateinit var binding: MeterListFragmentBinding
    lateinit var location: Location

    // TODO Two ViewModels...
    private val locationViewModel: LocationViewModel by viewModels {
        LocationViewModelFactory((activity?.application as MetersApplication).locationRepository)
    }

    private val meterViewModel: MeterViewModel by viewModels {
        MeterViewModelFactory((activity?.application as MetersApplication).meterRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = MeterListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: MeterListFragmentArgs by navArgs()
        val locationId = args.locationId
        binding.meterRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        val meterAdapter = MeterListAdapter()
        binding.meterRecyclerview.adapter = meterAdapter

        meterViewModel.getAllMetersByLocationId(locationId).observe(this, Observer {
            it?.let { meterAdapter.submitList(it) }
        })

        // TODO remove temp button
        binding.meterDeleteBtn.setOnClickListener {
            meterViewModel.deleteAllMetersByLocationId(locationId)
        }

        val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        locationViewModel.getLocationById(locationId = locationId).observe(this, {
            location = it
            actionBar?.title = location.name
            binding.meterDescription.text = location.description
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.meter_list_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete_location -> {
                lateinit var dialog: AlertDialog
                val builder = AlertDialog.Builder(this.context)
                builder.setTitle(resources.getString(R.string.location_delete_dialog_title))
                builder.setMessage(
                    resources.getString(R.string.location_delete_dialog_message)
                            + ' '
                            + location.name
                            + '?'
                )
                val dialogClickListener = DialogInterface.OnClickListener { _, which ->
                    when (which) {
                        DialogInterface.BUTTON_POSITIVE -> {
                            locationViewModel.deleteLocation(location = location)
                            Toast.makeText(
                                requireActivity(),
                                resources.getString(R.string.location_delete_toast),
                                Toast.LENGTH_LONG
                            ).show()
                            findNavController().navigate(
                                MeterListFragmentDirections
                                    .actionMeterListFragmentToLocationListFragment()
                            )
                        }
                        DialogInterface.BUTTON_NEGATIVE -> dialog.dismiss()
                    }
                }
                builder.setPositiveButton(
                    resources.getString(R.string.location_delete_dialog_positive),
                    dialogClickListener
                )
                builder.setNegativeButton(
                    resources.getString(R.string.location_delete_dialog_negative),
                    dialogClickListener
                )
                dialog = builder.create()
                dialog.show()
                true
            }
            R.id.edit_location -> {
                findNavController().navigate(
                    MeterListFragmentDirections.actionMeterListFragmentToLocationDetailsFragment(location.id))
                true
            }
            R.id.add_meter -> {
                findNavController().navigate(
                    MeterListFragmentDirections.actionMeterListFragmentToMeterDetailsFragment(location.id, 0)
                )
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
