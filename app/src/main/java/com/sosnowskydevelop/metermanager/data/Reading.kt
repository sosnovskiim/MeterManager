package com.sosnowskydevelop.metermanager.data

import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import com.sosnowskydevelop.metermanager.Unit
import java.time.LocalDate

@Entity(
    tableName = "reading",
    foreignKeys = [ForeignKey(
        entity = Meter::class,
        parentColumns = arrayOf("_id"),
        childColumns = arrayOf("meterId"),
        onDelete = CASCADE)])
data class Reading(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    val id: Int,
    val meterId: Int,
    @TypeConverters(DateConverter::class)
    var date: LocalDate,
    var value: Float,
    var unit: Unit
)
