package com.sosnowskydevelop.metermanager.viewmodel

import androidx.lifecycle.*
import com.sosnowskydevelop.metermanager.data.Meter
import com.sosnowskydevelop.metermanager.repository.MeterRepository
import kotlinx.coroutines.*
import java.lang.IllegalArgumentException

class MeterViewModel(private val meterRepository: MeterRepository) : ViewModel() {
    fun getAllMetersByLocationId(locationId: Int): LiveData<List<Meter>> {
        return meterRepository.getAllMetersByLocationID(locationId)
    }

    fun getMeterById(meterId: Int): LiveData<Meter> {
        return meterRepository.getMeterByID(meterId)
    }

    fun insert(meter: Meter) = viewModelScope.launch {
        meterRepository.insert(meter)
    }

    fun update(meter: Meter) = viewModelScope.launch {
        meterRepository.update(meter)
    }

    fun deleteAllMetersByLocationId(locationId: Int) = viewModelScope.launch {
        meterRepository.deleteAllMetersByLocationId(locationId)
    }

    fun deleteMeter(meter: Meter) = viewModelScope.launch {
        meterRepository.deleteMeter(meter = meter)
    }

    fun isMeterDuplicate(
        meterId: Int,
        locationId: Int,
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
