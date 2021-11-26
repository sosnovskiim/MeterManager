package com.sosnowskydevelop.metermanager.repository

import androidx.annotation.WorkerThread
import com.sosnowskydevelop.metermanager.dao.LocationDao
import com.sosnowskydevelop.metermanager.data.Location
import com.sosnowskydevelop.metermanager.data.Meter
import kotlinx.coroutines.flow.Flow

class LocationRepository(private val locationDao: LocationDao) {
    val allLocations: Flow<List<Location>> = locationDao.getAllLocations()

    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    fun getLocationByID(locationId: Int): Flow<Location> {
        return locationDao.getLocationByID(locationId)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(location: Location) {
        locationDao.insert(location)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteAllLocations() {
        locationDao.deleteAllLocations()
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteLocation(location: Location) {
        locationDao.delete(location = location)
    }
}