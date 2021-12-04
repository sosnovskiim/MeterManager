package com.sosnowskydevelop.metermanager.data

import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import com.sosnowskydevelop.metermanager.Unit
import java.time.LocalDate

@Entity(
    tableName = "meter",
    foreignKeys = [
        ForeignKey(
            entity = Location::class,
            parentColumns = arrayOf("_id"),
            childColumns = arrayOf("locationId"),
            onDelete = CASCADE)
        , ForeignKey(
            entity = Reading::class,
            parentColumns = arrayOf("_id"),
            childColumns = arrayOf("lastReadingID"))
            ])
data class Meter(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    val id: Int,
    val locationId: Int,
    var name: String,
    var unit: Unit
) {
    @Ignore
    private var lastReading: Reading? = null

    @ColumnInfo(name="lastReadingID")
    var lastReadingId: Int? = null

    fun addLastReading(reading: Reading) {
        lastReading = reading
        lastReadingId = reading.id
    }

    fun getLastReadingDate(): LocalDate? {
        return lastReading?.date
    }
}