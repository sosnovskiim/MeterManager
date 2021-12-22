package com.sosnowskydevelop.metermanager.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "location")
data class Location(
    @PrimaryKey
    @ColumnInfo(name = "_id")
    val id: String,
    var name: String,
    var description: String?) : Serializable