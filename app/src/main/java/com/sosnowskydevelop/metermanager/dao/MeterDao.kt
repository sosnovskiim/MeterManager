package com.sosnowskydevelop.metermanager.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.sosnowskydevelop.metermanager.data.Meter

@Dao
interface MeterDao {

    @Query("SELECT * FROM meter WHERE locationId = :locationID ORDER BY _id DESC")
    fun getAllMetersByLocationID(locationID: String?): LiveData<List<Meter>>

    @Query("SELECT * FROM meter WHERE _id = :meterId")
    fun getMeterByID(meterId: String?): LiveData<Meter>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(meter: Meter)

    @Update
    suspend fun update(meter: Meter?)

    @Delete
    suspend fun delete(meter: Meter?)

    @Query("SELECT 1 FROM meter WHERE _id != coalesce(:meterId, '') AND locationId = coalesce(:locationID, '') AND name = :name")
    fun isMeterDuplicate(
        meterId: String?,
        locationID: String?,
        name: String,
    ): LiveData<String>
}