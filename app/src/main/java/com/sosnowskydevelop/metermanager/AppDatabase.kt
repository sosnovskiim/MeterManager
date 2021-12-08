package com.sosnowskydevelop.metermanager

import android.content.Context
import androidx.room.*
import androidx.room.migration.AutoMigrationSpec
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

//-list -v -alias androiddebugkey -keystore %USERPROFILE%\.android\debug.keystore

//81:E6:97:B5:A5:5D:D4:86:F2:80:E0:5D:33:64:A0:D2:A4:3E:C5:20