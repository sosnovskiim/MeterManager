package com.sosnowskydevelop.metermanager.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.sosnowskydevelop.metermanager.dao.MeterDao
import com.sosnowskydevelop.metermanager.data.Meter
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class MeterRepository(private val meterDao: MeterDao) {

    @DelicateCoroutinesApi
    @ExperimentalCoroutinesApi
    fun getAllMetersByLocationID(locationId: Int): List<Meter> {
        val getDataJob =
            GlobalScope.async { meterDao.getAllMetersByLocationID(locationID = locationId) }

        var result: List<Meter> = listOf()
        getDataJob.invokeOnCompletion { cause ->
            if (cause == null) {
                result = getDataJob.getCompleted()
            }
        }
        return result
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    fun getMeterByID(meterId: Int): LiveData<Meter> {
        return meterDao.getMeterByID(meterId = meterId)
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

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteMeter(meter: Meter) {
        meterDao.delete(meter = meter)
    }
}