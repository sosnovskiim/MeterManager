package com.sosnowskydevelop.metermanager.viewmodel

import androidx.lifecycle.*
import com.sosnowskydevelop.metermanager.data.Location
import com.sosnowskydevelop.metermanager.repository.LocationRepository
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class LocationViewModel(private val locationRepository: LocationRepository) : ViewModel() {

    val allLocations: LiveData<List<Location>> = locationRepository.allLocations

    fun getLocationById(locationId: Int): LiveData<Location> {
        return locationRepository.getLocationByID(locationId)
    }

    fun insert(location: Location) = viewModelScope.launch {
        locationRepository.insert(location)
    }

    fun update(location: Location) = viewModelScope.launch {
        locationRepository.update(location)
    }

    fun deleteLocation(location: Location) = viewModelScope.launch {
        locationRepository.deleteLocation(location = location)
    }

    fun isLocationNameUnique(name: String, id: Int): Boolean {
        allLocations.value?.forEach {
            if (it.name == name && it.id != id) {
                return false
            }
        }
        return true
    }
}

class LocationViewModelFactory(private val repository: LocationRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LocationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LocationViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
