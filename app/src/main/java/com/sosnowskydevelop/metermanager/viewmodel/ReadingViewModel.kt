package com.sosnowskydevelop.metermanager.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.sosnowskydevelop.metermanager.data.Reading
import com.sosnowskydevelop.metermanager.repository.ReadingRepository
import kotlinx.coroutines.launch

class ReadingViewModel(private val readingRepository: ReadingRepository) : ViewModel() {

    fun getAllReadingsByMeterId(meterId: String?): LiveData<List<Reading>> {
        return readingRepository.getAllReadingsByMeterID(meterId = meterId)
    }

    fun getReadingById(readingId: String?): LiveData<Reading> {
        return readingRepository.getReadingByID(readingId = readingId)
    }

    fun getLastReadingByMeterID(meterId: String?): LiveData<Reading> {
        return readingRepository.getLastReadingByMeterID(meterId = meterId)
    }

    fun insert(reading: Reading) = viewModelScope.launch {
        readingRepository.insert(reading = reading)
    }

    fun update(reading: Reading?) = viewModelScope.launch {
        readingRepository.update(reading = reading)
    }

    fun deleteAllReadingsByMeterId(meterId: String) = viewModelScope.launch {
        readingRepository.deleteAllReadingsByMeterId(meterId = meterId)
    }

    fun delete(reading: Reading?) = viewModelScope.launch {
        readingRepository.delete(reading = reading)
    }

    fun addLastReading(reading: Reading?) = viewModelScope.launch {
        readingRepository.addLastReading(meterId = reading?.meterId, date = reading?.date, value = reading?.value)
    }
}

class ReadingViewModelFactory(private val repository: ReadingRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReadingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ReadingViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}