package com.sosnowskydevelop.metermanager.dao

import androidx.room.*
import com.sosnowskydevelop.metermanager.data.Meter
import kotlinx.coroutines.flow.Flow

@Dao
interface MeterDao {

    @Query("SELECT * FROM meter WHERE locationId = :locationID ORDER BY _id")
    fun getAllMetersByLocationID(locationID: Int): Flow<List<Meter>>

    @Query("SELECT * FROM meter WHERE _id = :id")
    fun getMeterByID(id: Int): Meter?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(meter: Meter)

    @Update
    suspend fun update(meter: Meter)

    @Delete
    suspend fun delete(meter: Meter)

    @Query("DELETE FROM meter WHERE locationId = :locationID")
    suspend fun deleteAllMetersByLocationID(locationID: Int)
}