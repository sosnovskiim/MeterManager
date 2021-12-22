package com.sosnowskydevelop.metermanager.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sosnowskydevelop.metermanager.dao.ReadingDao
import com.sosnowskydevelop.metermanager.data.Reading
import java.util.*

class ReadingRepository(private val readingDao: ReadingDao) {

    @WorkerThread
    fun getAllReadingsByMeterID(meterId: String?): LiveData<List<Reading>> {
        return readingDao.getAllReadingsByMeterID(meterID = meterId)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    fun getReadingByID(readingId: String?): LiveData<Reading> {
        return readingDao.getReadingByID(readingId = readingId)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    fun getLastReadingByMeterID(meterId: String?): LiveData<Reading> {
        return readingDao.getLastReadingByMeterID(meterId = meterId)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(reading: Reading) {
        readingDao.insert(reading = reading)
        save(reading)
    }

    private fun save(reading: Reading) {
        val db = Firebase.firestore
        db.collection("reading").document(reading.id).set(reading)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun update(reading: Reading?) {
        readingDao.update(reading = reading)
        reading?.let {save(reading = reading)}
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteAllReadingsByMeterId(meterId: String) {
        readingDao.deleteAllReadingsByMeterID(meterID = meterId)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(reading: Reading?) {
        readingDao.delete(reading = reading)
        reading?.let {deleteFromFirebase(reading = reading)}
    }

    private fun deleteFromFirebase(reading: Reading) {
        val db = Firebase.firestore
        db.collection("reading").document(reading.id).delete()
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun addLastReading(meterId: String?, date: Date?, value: Float?) {
        readingDao.addLastReadingToMeter(meterId = meterId, date = date, value = value)
        meterId?.let {addLastReadingToFirebase(meterId, date, value)}
    }

    private fun addLastReadingToFirebase(meterId: String, date: Date?, value: Float?) {
        val db = Firebase.firestore
        val data = hashMapOf(
            "lastReadingDate" to date,
            "lastReadingValue" to value
        )
        db.collection("meter").document(meterId).set(data, SetOptions.merge())
    }
}