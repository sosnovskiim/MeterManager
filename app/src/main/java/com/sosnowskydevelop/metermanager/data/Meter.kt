package com.sosnowskydevelop.metermanager.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import com.sosnowskydevelop.metermanager.Unit

@Entity(
    tableName = "meter",
    foreignKeys = [ForeignKey(
        entity = Location::class,
        parentColumns = arrayOf("_id"),
        childColumns = arrayOf("locationId"),
        onDelete = CASCADE)])
data class Meter(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    val id: Int,
    val locationId: Int,
    val name: String,
    val unit: Unit
) {

}