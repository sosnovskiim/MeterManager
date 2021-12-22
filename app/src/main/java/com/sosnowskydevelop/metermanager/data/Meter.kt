package com.sosnowskydevelop.metermanager.data

import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import com.sosnowskydevelop.metermanager.Unit
import java.io.Serializable
import java.util.*

@Entity(
    tableName = "meter",
    foreignKeys = [
        ForeignKey(
            entity = Location::class,
            parentColumns = arrayOf("_id"),
            childColumns = arrayOf("locationId"),
            onDelete = CASCADE)])
data class Meter(
    @PrimaryKey
    @ColumnInfo(name = "_id")
    val id: String,
    val locationId: String,
    var name: String,
    var unit: Unit
) : Serializable {
    @ColumnInfo(name="lastReadingDate")
    @TypeConverters(DateConverter::class)
    var lastReadingDate: Date? = null

    @ColumnInfo(name="lastReadingValue")
    var lastReadingValue: Float? = 0f
}