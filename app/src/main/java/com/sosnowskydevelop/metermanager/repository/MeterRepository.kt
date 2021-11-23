package com.sosnowskydevelop.metermanager.repository

import androidx.annotation.WorkerThread
import com.sosnowskydevelop.metermanager.dao.MeterDao
import com.sosnowskydevelop.metermanager.data.Meter
import kotlinx.coroutines.flow.Flow

class MeterRepository(private val meterDao: MeterDao) {

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    fun getAllMetersBuLocationID(locationId: Int): Flow<List<Meter>> {
        return meterDao.getAllMetersByLocationID(locationId)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(meter: Meter) {
        meterDao.insert(meter)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteAllMetersByLocationId(locationId: Int) {
        meterDao.deleteAllMetersByLocationID(locationId)
    }
}