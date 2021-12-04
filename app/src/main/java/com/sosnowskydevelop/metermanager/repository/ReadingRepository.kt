package com.sosnowskydevelop.metermanager.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.sosnowskydevelop.metermanager.dao.ReadingDao
import com.sosnowskydevelop.metermanager.data.Reading

class ReadingRepository(private val readingDao: ReadingDao) {

    @WorkerThread
    fun getAllReadingsByMeterID(meterId: Int): LiveData<List<Reading>> {
        return readingDao.getAllReadingsByMeterID(meterId)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    fun getReadingByID(readingId: Int): LiveData<Reading> {
        return readingDao.getReadingByID(readingId = readingId)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(reading: Reading) {
        val id = readingDao.insert(reading).toInt()
        readingDao.addLastReadingToMeter(reading.meterId, id)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun update(reading: Reading) {
        readingDao.update(reading)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteAllReadingsByMeterId(meterId: Int) {
        readingDao.deleteAllReadingsByMeterID(meterId)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(reading: Reading) {
        readingDao.delete(reading = reading)
    }
}