package com.sosnowskydevelop.metermanager.data

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date

class DateConverter {

    @RequiresApi(Build.VERSION_CODES.O) // TODO хз чё это
    @TypeConverter
    fun longToDate(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @RequiresApi(Build.VERSION_CODES.O) // TODO хз чё это
    @TypeConverter
    fun dateToLong(date: Date?): Long? {
        return date?.time
    }

    companion object {
        private const val template = "dd.MM.yyyy"

        @SuppressLint("SimpleDateFormat")
        @RequiresApi(Build.VERSION_CODES.O) // TODO хз чё это
        fun dateToString(date: Date?): String {
            return if (date == null) {
                ""
            } else {
                val format = SimpleDateFormat(template)
                format.format(date)
            }
        }

        @SuppressLint("SimpleDateFormat")
        @RequiresApi(Build.VERSION_CODES.O) // TODO хз чё это
        fun stringToDate(string: String): Date? {
            val formatter: DateFormat = SimpleDateFormat(template)
            return formatter.parse(string)
        }
    }
}