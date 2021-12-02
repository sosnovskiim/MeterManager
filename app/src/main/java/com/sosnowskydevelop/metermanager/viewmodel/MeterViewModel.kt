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

    fun deleteAllMetersByLocationId(locationId: Int) = viewModelScope.launch {
        meterRepository.deleteAllMetersByLocationId(locationId)
    }

    fun deleteMeter(meter: Meter) = viewModelScope.launch {
        meterRepository.deleteMeter(meter = meter)
    }

    fun isMeterDuplicate(name: String, locationId: Int): LiveData<String> {
        return meterRepository.isMeterDuplicate(name = name, locationId = locationId)
//        var result = true
//        viewModelScope.launch {
//            suspend {
//                withContext(Dispatchers.Main) {
//                    result = meterRepository.isMeterUnique(name = name, locationId = locationId)
//                }
//            }.invoke()
//        }
//        return result
    }
//        meterRepository.isMeterUnique(name = name, locationId = locationId)

//        val some = viewModelScope.launch {
//            meterRepository.isMeterUnique(name, locationId)
//        }
//        val some = GlobalScope.async { meterRepository.isMeterUnique(name, locationId) }
//        some.invokeOnCompletion { cause ->
//            if (cause != null) {
//                // error!  Handle that here
//                Unit
//            } else {
////                val result = some
//                val compl = some.getCompleted()
//                val str = compl.toString()
//                val i = str.toInt()
////                if (result == 1) {
////                    isMeterUnique = false
////                }
//                Unit
//            }
//        }
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
