package com.sosnowskydevelop.metermanager.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.sosnowskydevelop.metermanager.dao.LocationDao
import com.sosnowskydevelop.metermanager.data.Location

class LocationRepository(private val locationDao: LocationDao) {
    val allLocations: LiveData<List<Location>> = locationDao.getAllLocations()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    fun getLocationByID(locationId: Int): LiveData<Location> {
        return locationDao.getLocationByID(locationId)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(location: Location) {
        locationDao.insert(location)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun update(location: Location) {
        locationDao.update(location)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteLocation(location: Location) {
        locationDao.delete(location = location)
    }
}
