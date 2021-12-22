package com.sosnowskydevelop.metermanager.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sosnowskydevelop.metermanager.dao.LocationDao
import com.sosnowskydevelop.metermanager.data.Location

class LocationRepository(private val locationDao: LocationDao) {
    val allLocations: LiveData<List<Location>> = locationDao.getAllLocations()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    fun getLocationByID(locationId: String?): LiveData<Location> {
        return locationDao.getLocationByID(id = locationId)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(location: Location) {
        locationDao.insert(location = location)
        saveToFirebase(location = location)
    }

    private fun saveToFirebase(location: Location) {
        val db = Firebase.firestore
        db.collection("location").document(location.id).set(location)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun update(location: Location) {
        locationDao.update(location = location)
        saveToFirebase(location)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteLocation(location: Location?) {
        locationDao.delete(location = location)
        location?.let {deleteFromFirebase(location = location)}
    }

    private fun deleteFromFirebase(location: Location) {
        val db = Firebase.firestore

        db.collection("location").document(location.id).delete()
    }
}
