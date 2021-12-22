package com.sosnowskydevelop.metermanager.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
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
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun update(reading: Reading?) {
        readingDao.update(reading = reading)
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
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun addLastReading(meterId: String?, date: Date?, value: Float?) {
        readingDao.addLastReadingToMeter(meterId = meterId, date = date, value = value)
    }
}