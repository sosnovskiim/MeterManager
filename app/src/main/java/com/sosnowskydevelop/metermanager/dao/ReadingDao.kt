package com.sosnowskydevelop.metermanager.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.sosnowskydevelop.metermanager.data.Reading

@Dao
interface ReadingDao {
    @Query("SELECT * FROM reading WHERE meterId = :meterID ORDER BY _id DESC")
    fun getAllReadingsByMeterID(meterID: Int): LiveData<List<Reading>>

    @Query("SELECT * FROM reading WHERE _id = :readingId")
    fun getReadingByID(readingId: Int): LiveData<Reading>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(reading: Reading): Long

    @Update
    suspend fun update(reading: Reading)

    @Delete
    suspend fun delete(reading: Reading)

    @Query("DELETE FROM reading WHERE meterId = :meterID")
    suspend fun deleteAllReadingsByMeterID(meterID: Int)

    @Query("UPDATE meter SET lastReadingID = :lastReadingId WHERE _id = :meterId")
    suspend fun addLastReadingToMeter(meterId: Int, lastReadingId: Int)
}