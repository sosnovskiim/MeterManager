package com.sosnowskydevelop.metermanager.fragment

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.sosnowskydevelop.metermanager.viewmodel.MeterViewModel
import com.sosnowskydevelop.metermanager.viewmodel.MeterViewModelFactory
import com.sosnowskydevelop.metermanager.MetersApplication
import com.sosnowskydevelop.metermanager.Unit
import com.sosnowskydevelop.metermanager.data.Meter
import com.sosnowskydevelop.metermanager.databinding.MeterDetailsFragmentBinding

class MeterDetailsFragment : Fragment() {
    private lateinit var binding: MeterDetailsFragmentBinding

    private val meterViewModel: MeterViewModel by viewModels {
        MeterViewModelFactory((activity?.application as MetersApplication).meterRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MeterDetailsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: MeterDetailsFragmentArgs by navArgs()
        val locationId = args.locationId
        binding.meterSaveBtn.setOnClickListener {
            if (TextUtils.isEmpty(binding.meterNameEdittext.text)) {
                // TODO check
            } else {
                val name = binding.meterNameEdittext.text.toString()
                meterViewModel.insert(Meter(0, locationId, name, Unit.KILOVAT)) // TODO Unit
                findNavController().navigate(MeterDetailsFragmentDirections.actionMeterDetailsFragmentToMeterListFragment(locationId))
            }
        }
    }
}