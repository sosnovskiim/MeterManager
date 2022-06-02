package com.sosnowskydevelop.metermanager.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.sosnowskydevelop.metermanager.data.Meter

@Dao
interface MeterDao {

    @Query("SELECT m._id, m.name, m.locationId, m.unit, v.[VALUE] AS lastReadingValue, d.[DATE] AS lastReadingDate " +
           "FROM meter AS m " +
           "LEFT JOIN (SELECT meterID, MAX(value) AS value, date FROM reading GROUP BY meterID, date) AS v ON m._id = v.meterId " +
           "LEFT JOIN (SELECT meterID, MAX(date) AS date FROM reading GROUP BY meterID) AS d ON v.date = d.date AND m._id = d.meterID " +
           "WHERE m.locationId = :locationID")
    fun getAllMetersByLocationID(locationID: Int): LiveData<List<Meter>>

    @Query("SELECT * FROM meter WHERE _id = :meterId")
    fun getMeterByID(meterId: Int): LiveData<Meter>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(meter: Meter)

    @Update
    suspend fun update(meter: Meter)

    @Delete
    suspend fun delete(meter: Meter)

    @Query("SELECT 1 FROM meter WHERE _id != :meterId AND locationId = :locationID AND name = :name")
    fun isMeterDuplicate(
        meterId: Int,
        locationID: Int,
        name: String,
    ): LiveData<String>
}