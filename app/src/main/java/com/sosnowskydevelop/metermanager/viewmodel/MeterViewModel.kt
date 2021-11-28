package com.sosnowskydevelop.metermanager.viewmodel

import androidx.lifecycle.*
import com.sosnowskydevelop.metermanager.data.Location
import com.sosnowskydevelop.metermanager.data.Meter
import com.sosnowskydevelop.metermanager.repository.MeterRepository
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class MeterViewModel(private val meterRepository: MeterRepository) : ViewModel() {

    fun getAllMetersByLocationId(locationId: Int): LiveData<List<Meter>> {
       return meterRepository.getAllMetersBuLocationID(locationId).asLiveData()
    }

    fun getMeterById(meterId: Int): LiveData<Meter> {
        return meterRepository.getMeterByID(meterId)
    }

    fun insert(meter: Meter) = viewModelScope.launch {
        meterRepository.insert(meter)
    }

    fun deleteAllMetersByLocationId(locationId: Int) = viewModelScope.launch {
        meterRepository.deleteAllMetersByLocationId(locationId)
    }

    fun deleteMeter(meter: Meter) = viewModelScope.launch {
        meterRepository.deleteMeter(meter = meter)
    }
}

/**
 * By using viewModels and ViewModelProvider.Factory,the framework will take care of the lifecycle of the ViewModel.
 * It will survive configuration changes and even if the Activity is recreated, you'll always get the right instance
 * of the WordViewModel class.
 */
class MeterViewModelFactory(private val repository: MeterRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MeterViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MeterViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}