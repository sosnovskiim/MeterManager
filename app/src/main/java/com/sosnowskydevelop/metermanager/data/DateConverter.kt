package com.sosnowskydevelop.metermanager.data

import android.annotation.SuppressLint
import androidx.room.TypeConverter
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date

class DateConverter {

    @TypeConverter
    fun longToDate(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToLong(date: Date?): Long? {
        return date?.time
    }

    companion object {
        private const val template = "dd.MM.yyyy"

        @SuppressLint("SimpleDateFormat")
        fun dateToString(date: Date?): String {
            return if (date == null) {
                ""
            } else {
                val format = SimpleDateFormat(template)
                format.format(date)
            }
        }

        @SuppressLint("SimpleDateFormat")
        fun stringToDate(string: String): Date? {
            val formatter: DateFormat = SimpleDateFormat(template)
            return formatter.parse(string)
        }
    }
}