package com.sosnowskydevelop.metermanager

import android.app.Application
import com.sosnowskydevelop.metermanager.repository.LocationRepository
import com.sosnowskydevelop.metermanager.repository.MeterRepository
import com.sosnowskydevelop.metermanager.repository.ReadingRepository
import com.sosnowskydevelop.metermanager.repository.Repository

class MetersApplication : Application() {
    private val database by lazy { AppDatabase.getDatabase(this) }

    private val repository: Repository by lazy {
        Repository(
            locationDao = database.locationDao(),
            meterDao = database.meterDao(),
            readingDao = database.readingDao(),
        )
    }

    val locationRepository by lazy { LocationRepository(repository, database.locationDao()) }
    val meterRepository by lazy { MeterRepository(database.meterDao()) }
    val readingRepository by lazy { ReadingRepository(database.readingDao()) }
}