package com.sosnowskydevelop.metermanager.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.sosnowskydevelop.metermanager.data.Location
import com.sosnowskydevelop.metermanager.repository.LocationRepository
import kotlinx.coroutines.launch

class LocationViewModel(private val locationRepository: LocationRepository) : ViewModel() {

    val allLocations: LiveData<List<Location>> = locationRepository.allLocations

    fun getLocationById(locationId: String?): LiveData<Location> {
        return locationRepository.getLocationByID(locationId = locationId)
    }

    fun insert(location: Location) = viewModelScope.launch {
        locationRepository.insert(location = location)
    }

    fun update(location: Location) = viewModelScope.launch {
        locationRepository.update(location = location)
    }

    fun deleteLocation(location: Location?) = viewModelScope.launch {
        locationRepository.deleteLocation(location = location)
    }

    fun isLocationNameUnique(name: String, id: String): Boolean {
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
