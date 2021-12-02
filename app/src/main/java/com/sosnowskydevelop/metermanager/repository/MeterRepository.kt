package com.sosnowskydevelop.metermanager.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.sosnowskydevelop.metermanager.dao.MeterDao
import com.sosnowskydevelop.metermanager.data.Meter

class MeterRepository(private val meterDao: MeterDao) {

    //@Suppress("RedundantSuspendModifier")
    @WorkerThread
    fun getAllMetersByLocationID(locationId: Int): LiveData<List<Meter>> {
        return meterDao.getAllMetersByLocationID(locationId)
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

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    fun isMeterDuplicate(name: String, locationId: Int): LiveData<String> {
        return meterDao.isMeterDuplicate(name = name, locationID = locationId)
//        val temp = meterDao.isMeterUnique(name = name, locationID = locationId) != "1"
//        Log.i("metermanager", temp.toString())
//        return temp
////        return false
    }
}