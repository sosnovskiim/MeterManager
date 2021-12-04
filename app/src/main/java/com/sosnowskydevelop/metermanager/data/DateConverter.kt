package com.sosnowskydevelop.metermanager.data

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class DateConverter {

    @RequiresApi(Build.VERSION_CODES.O) // TODO хз чё это
    @TypeConverter
    fun longToDate(value: Long?): LocalDate? {
        return value?.let { Date(it).toInstant().atZone(ZoneId.systemDefault()).toLocalDate() }
    }

    @RequiresApi(Build.VERSION_CODES.O) // TODO хз чё это
    @TypeConverter
    fun dateToLong(date: LocalDate?): Long? {
        return date?.toEpochDay()
    }

    companion object {
        private const val template = "dd.MM.yyyy"

        @RequiresApi(Build.VERSION_CODES.O) // TODO хз чё это
        fun dateToString(date: LocalDate?): String {
            return if (date == null) {
                ""
            } else {
                date.format (DateTimeFormatter.ofPattern(template))
            }
        }

        @RequiresApi(Build.VERSION_CODES.O) // TODO хз чё это
        fun stringToDate(string: String): LocalDate {
            val formatter = DateTimeFormatter.ofPattern(template)
            return LocalDate.parse(string, formatter)
        }
    }
}