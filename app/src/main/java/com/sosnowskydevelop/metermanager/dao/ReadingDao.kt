package com.sosnowskydevelop.metermanager.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.sosnowskydevelop.metermanager.data.Reading
import java.util.*

@Dao
interface ReadingDao {
    @Query("SELECT * FROM reading WHERE meterId = :meterID ORDER BY date DESC, value DESC, _id DESC")
    fun getAllReadingsByMeterID(meterID: String?): LiveData<List<Reading>>

    @Query("SELECT * FROM reading WHERE _id = :readingId")
    fun getReadingByID(readingId: String?): LiveData<Reading>

    @Query("SELECT * FROM reading WHERE meterId = :meterId ORDER BY date DESC, value DESC, _id DESC LIMIT 1")
    fun getLastReadingByMeterID(meterId: String?): LiveData<Reading>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(reading: Reading)

    @Update
    suspend fun update(reading: Reading?)

    @Delete
    suspend fun delete(reading: Reading?)

    @Query("DELETE FROM reading WHERE meterId = :meterID")
    suspend fun deleteAllReadingsByMeterID(meterID: String)

    @Query("UPDATE meter SET lastReadingDate = :date, lastReadingValue = :value WHERE _id = :meterId")
    suspend fun addLastReadingToMeter(meterId: String?, date: Date?, value: Float?)
}