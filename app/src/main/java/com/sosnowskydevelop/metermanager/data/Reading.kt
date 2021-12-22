package com.sosnowskydevelop.metermanager.data

import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import com.sosnowskydevelop.metermanager.Unit
import java.io.Serializable
import java.util.*

@Entity(
    tableName = "reading",
    foreignKeys = [ForeignKey(
        entity = Meter::class,
        parentColumns = arrayOf("_id"),
        childColumns = arrayOf("meterId"),
        onDelete = CASCADE)])
data class Reading(
    @PrimaryKey
    @ColumnInfo(name = "_id")
    val id: String,
    val meterId: String,
    @TypeConverters(DateConverter::class)
    var date: Date?,
    var value: Float?,
    var unit: Unit
) : Serializable
