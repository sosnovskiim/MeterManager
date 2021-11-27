package com.sosnowskydevelop.metermanager.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "location")
data class Location(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    val id: Int,
    var name: String,
    var description: String?) {
    // TODO add list of meters
}