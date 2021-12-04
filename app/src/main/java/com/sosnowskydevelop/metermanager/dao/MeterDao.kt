package com.sosnowskydevelop.metermanager.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.sosnowskydevelop.metermanager.data.Meter

@Dao
interface MeterDao {

    @Query("SELECT * FROM meter WHERE locationId = :locationID ORDER BY _id DESC")
    fun getAllMetersByLocationID(locationID: Int): LiveData<List<Meter>>

    @Query("SELECT * FROM meter WHERE _id = :meterId")
    fun getMeterByID(meterId: Int): LiveData<Meter>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(meter: Meter)

    @Update
    suspend fun update(meter: Meter)

    @Delete
    suspend fun delete(meter: Meter)

    @Query("DELETE FROM meter WHERE locationId = :locationID")
    suspend fun deleteAllMetersByLocationID(locationID: Int)

    @Query("SELECT 1 FROM meter WHERE name = :name AND locationId = :locationID")
    fun isMeterDuplicate(name: String, locationID: Int): LiveData<String>
}