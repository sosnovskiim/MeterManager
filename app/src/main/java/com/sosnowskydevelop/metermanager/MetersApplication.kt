package com.sosnowskydevelop.metermanager

import android.app.Application
import com.sosnowskydevelop.metermanager.repository.LocationRepository
import com.sosnowskydevelop.metermanager.repository.MeterRepository
import com.sosnowskydevelop.metermanager.repository.ReadingRepository

class MetersApplication : Application() {
    private val database by lazy { AppDatabase.getDatabase(this) }
    val locationRepository by lazy { LocationRepository(database.locationDao()) }
    val meterRepository by lazy { MeterRepository(database.meterDao()) }
    val readingRepository by lazy { ReadingRepository(database.readingDao()) }
}