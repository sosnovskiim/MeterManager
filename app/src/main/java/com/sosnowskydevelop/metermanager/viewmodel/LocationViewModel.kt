package com.sosnowskydevelop.metermanager

import androidx.lifecycle.*
import com.sosnowskydevelop.metermanager.data.Location
import com.sosnowskydevelop.metermanager.data.Meter
import com.sosnowskydevelop.metermanager.repository.LocationRepository
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class LocationViewModel(private val locationRepository: LocationRepository) : ViewModel() {

    // added a public LiveData member variable to cache the list of words.
    val allLocations: LiveData<List<Location>> = locationRepository.allLocations.asLiveData()

    fun getLocationById(locationId: Int): LiveData<Location> {
        return locationRepository.getLocationByID(locationId).asLiveData()
    }

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(location: Location) = viewModelScope.launch {
        locationRepository.insert(location)
    }

    fun deleteAll() = viewModelScope.launch {
        locationRepository.deleteAllLocations()
    }

    fun deleteLocation(location: Location) = viewModelScope.launch {
        locationRepository.deleteLocation(location = location)
    }
}

/**
 * By using viewModels and ViewModelProvider.Factory,the framework will take care of the lifecycle of the ViewModel.
 * It will survive configuration changes and even if the Activity is recreated, you'll always get the right instance
 * of the WordViewModel class.
 */
class LocationViewModelFactory(private val repository: LocationRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LocationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LocationViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}