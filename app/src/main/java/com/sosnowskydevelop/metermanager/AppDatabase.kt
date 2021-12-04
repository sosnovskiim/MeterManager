package com.sosnowskydevelop.metermanager

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sosnowskydevelop.metermanager.dao.LocationDao
import com.sosnowskydevelop.metermanager.dao.MeterDao
import com.sosnowskydevelop.metermanager.dao.ReadingDao
import com.sosnowskydevelop.metermanager.data.DateConverter
import com.sosnowskydevelop.metermanager.data.Location
import com.sosnowskydevelop.metermanager.data.Meter
import com.sosnowskydevelop.metermanager.data.Reading

@Database(entities = [Location::class, Meter::class, Reading::class], version = 6, exportSchema = true)
@TypeConverters(DateConverter::class)
abstract class AppDatabase: RoomDatabase() {

    abstract fun locationDao(): LocationDao
    abstract fun meterDao(): MeterDao
    abstract fun readingDao(): ReadingDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "meters_database"
                )
                    .fallbackToDestructiveMigration() // TODO add migration
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}