package com.sosnowskydevelop.metermanager.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sosnowskydevelop.metermanager.dao.MeterDao
import com.sosnowskydevelop.metermanager.data.Meter

class MeterRepository(private val meterDao: MeterDao) {

    //@Suppress("RedundantSuspendModifier")
    @WorkerThread
    fun getAllMetersByLocationID(locationId: String?): LiveData<List<Meter>> {
        return meterDao.getAllMetersByLocationID(locationID = locationId)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    fun getMeterByID(meterId: String?): LiveData<Meter> {
        return meterDao.getMeterByID(meterId = meterId)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(meter: Meter) {
        meterDao.insert(meter = meter)
        save(meter)
    }

    private fun save(meter: Meter) {
        val db = Firebase.firestore
        db.collection("meter").document(meter.id).set(meter)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun update(meter: Meter?) {
        meterDao.update(meter = meter)
        meter?.let {save(meter = meter)}
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteMeter(meter: Meter?) {
        meterDao.delete(meter = meter)
        meter?.let {deleteFromFirebase(meter = meter)}
    }

    private fun deleteFromFirebase(meter: Meter) {
        val db = Firebase.firestore
        db.collection("meter").document(meter.id).delete()
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    fun isMeterDuplicate(
        meterId: String?,
        locationId: String?,
        name: String,
    ): LiveData<String> {
        return meterDao.isMeterDuplicate(
            meterId = meterId,
            locationID = locationId,
            name = name,
        )
    }
}