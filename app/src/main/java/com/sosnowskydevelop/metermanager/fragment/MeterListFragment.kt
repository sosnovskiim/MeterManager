package com.sosnowskydevelop.metermanager.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.sosnowskydevelop.metermanager.adapter.MeterListAdapter
import com.sosnowskydevelop.metermanager.MeterViewModel
import com.sosnowskydevelop.metermanager.MeterViewModelFactory
import com.sosnowskydevelop.metermanager.MetersApplication
import com.sosnowskydevelop.metermanager.databinding.MeterListFragmentBinding

class MeterListFragment : Fragment() {
    private lateinit var binding: MeterListFragmentBinding

    private val meterViewModel: MeterViewModel by viewModels {
        MeterViewModelFactory((activity?.application as MetersApplication).meterRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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

        meterViewModel.getAllMetersByLocationId(locationId).observe(this, Observer { meter ->
            meter?.let { meterAdapter.submitList(it) }
        })

        binding.meterAddBtn.setOnClickListener {
            findNavController().navigate(MeterListFragmentDirections.actionMeterListFragmentToMeterDetailsFragment(locationId))
        }

        binding.meterDeleteBtn.setOnClickListener{
            meterViewModel.deleteAllMetersByLocationId(locationId)
        }

        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}