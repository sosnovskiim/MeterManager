package com.sosnowskydevelop.metermanager.repository

import android.app.Application
import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sosnowskydevelop.metermanager.dao.LocationDao
import com.sosnowskydevelop.metermanager.dao.MeterDao
import com.sosnowskydevelop.metermanager.dao.ReadingDao
import com.sosnowskydevelop.metermanager.data.Location

class Repository(
    private val locationDao: LocationDao,
    private val meterDao: MeterDao,
    private val readingDao: ReadingDao,
) {
    private val firebase = Firebase.firestore

//    @Suppress("RedundantSuspendModifier")
//    @WorkerThread
    suspend fun deleteLocation(location: Location) {
        meterDao.getAllMetersByLocationID(locationID = location.id).observeForever { meters ->
            meters.forEach { meter ->
                readingDao.getAllReadingsByMeterID(meterID = meter.id).observeForever { readings ->
                    readings.forEach { reading ->
                        firebase.collection("reading").document(reading.id).delete()
                    }
                }

                firebase.collection("meter").document(meter.id).delete()
            }
        }

        firebase.collection("location").document(location.id).delete()

//        locationDao.delete(location = location)
    }
}