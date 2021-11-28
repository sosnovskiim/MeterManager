package com.sosnowskydevelop.metermanager.viewmodel

import androidx.lifecycle.*
import com.sosnowskydevelop.metermanager.data.Meter
import com.sosnowskydevelop.metermanager.repository.MeterRepository
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class MeterViewModel(private val meterRepository: MeterRepository) : ViewModel() {

    fun getAllMetersByLocationId(locationId: Int): LiveData<List<Meter>> {
       return meterRepository.getAllMetersByLocationID(locationId)
    }

    fun insert(meter: Meter) = viewModelScope.launch {
        meterRepository.insert(meter)
    }

    fun deleteAllMetersByLocationId(locationId: Int) = viewModelScope.launch {
        meterRepository.deleteAllMetersByLocationId(locationId)
    }

//    fun isMeterNameUnique(locationId: Int, name: String): Boolean {
//        getAllMetersByLocationId(locationId).observe() value?.forEach {
//           if (it.name == name) {
//               return false
//           }
//        }
//        return true
//    }
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