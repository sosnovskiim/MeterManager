package com.sosnowskydevelop.metermanager.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.sosnowskydevelop.metermanager.data.Location

@Dao
interface LocationDao {

    @Query("SELECT * FROM location ORDER BY _id DESC")
    fun getAllLocations(): LiveData<List<Location>>

    @Query("SELECT * FROM location WHERE _id = :id")
    fun getLocationByID(id: Int): LiveData<Location>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(location: Location)

    @Update
    suspend fun update(location: Location)

    @Delete
    suspend fun delete(location: Location)

    @Query("DELETE FROM location")
    suspend fun deleteAllLocations()
}