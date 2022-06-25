package com.sosnowskydevelop.metermanager.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.sosnowskydevelop.metermanager.data.Meter

@Dao
interface MeterDao {

    @Query("SELECT m._id, m.name, m.locationId, m.unit, max(r.value) AS lastReadingValue, max(r.date) AS lastReadingDate " +
            "FROM meter AS m " +
            "LEFT JOIN reading AS r ON m._id = r.meterId " +
            "WHERE m.locationId = :locationID " +
            "GROUP BY m._id, m.name, m.locationId, m.unit")
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