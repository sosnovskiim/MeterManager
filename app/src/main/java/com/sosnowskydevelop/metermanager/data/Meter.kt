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
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    val id: Int,
    val locationId: Int,
    var name: String,
    var unit: Unit
) : Serializable {
    @ColumnInfo(name="lastReadingDate")
    @TypeConverters(DateConverter::class)
    var lastReadingDate: Date? = null

    @ColumnInfo(name="lastReadingValue")
    var lastReadingValue: Float = 0f
}