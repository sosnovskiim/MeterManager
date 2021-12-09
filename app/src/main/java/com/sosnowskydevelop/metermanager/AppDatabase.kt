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

@Database(
    version = 1,
    entities = [
        Location::class,
        Meter::class,
        Reading::class
               ],
//    autoMigrations = [
//        AutoMigration(from = 1, to = 2),
//        AutoMigration(from = 2, to = 3, spec = AppDatabase.AppAutoMigration::class)
//                     ],
    exportSchema = true)
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
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    /* Database migration example */
//    @DeleteColumn(tableName = "location", columnName = "tmp")
//    class AppAutoMigration: AutoMigrationSpec {}
}