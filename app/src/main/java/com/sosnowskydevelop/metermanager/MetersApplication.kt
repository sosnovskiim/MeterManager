package com.sosnowskydevelop.metermanager

import android.app.Application
import com.sosnowskydevelop.metermanager.repository.LocationRepository
import com.sosnowskydevelop.metermanager.repository.MeterRepository

class MetersApplication : Application() {
    val database by lazy { AppDatabase.getDatabase(this) }
    val locationRepository by lazy { LocationRepository(database.locationDao()) }
    val meterRepository by lazy { MeterRepository(database.meterDao()) }
}