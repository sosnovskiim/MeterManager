package com.sosnowskydevelop.metermanager.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.sosnowskydevelop.metermanager.data.Meter
import com.sosnowskydevelop.metermanager.repository.MeterRepository
import kotlinx.coroutines.launch

class MeterViewModel(private val meterRepository: MeterRepository) : ViewModel() {
    fun getAllMetersByLocationId(locationId: String?): LiveData<List<Meter>> {
        return meterRepository.getAllMetersByLocationID(locationId = locationId)
    }

    fun getMeterById(meterId: String?): LiveData<Meter> {
        return meterRepository.getMeterByID(meterId = meterId)
    }

    fun insert(meter: Meter) = viewModelScope.launch {
        meterRepository.insert(meter = meter)
    }

    fun update(meter: Meter?) = viewModelScope.launch {
        meterRepository.update(meter = meter)
    }

    fun deleteMeter(meter: Meter?) = viewModelScope.launch {
        meterRepository.deleteMeter(meter = meter)
    }

    fun isMeterDuplicate(
        meterId: String?,
        locationId: String?,
        name: String,
    ): LiveData<String> {
        return meterRepository.isMeterDuplicate(
            meterId = meterId,
            locationId = locationId,
            name = name,
        )
    }
}

class MeterViewModelFactory(private val repository: MeterRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MeterViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MeterViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
