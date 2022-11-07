package ar.edu.ort.jefud_notifying_system.viewmodel

import android.util.Log
import androidx.lifecycle.*

import ar.edu.ort.jefud_notifying_system.dao.FailureDao

import ar.edu.ort.jefud_notifying_system.model.Failure
import ar.edu.ort.jefud_notifying_system.model.Message
import kotlinx.coroutines.launch

class FailuresViewModel(private val failureDao: FailureDao): ViewModel() {
    val allFailures: LiveData<List<Failure>> = failureDao.getAll().asLiveData()

    fun addNewFailure(panel: String, task: String, plant: String, equipment: String, value: String) {
        val newFailure = getNewFailureEntry(panel, task, plant, equipment, value)
        insertFailure(newFailure)
    }

    fun delete(failure: Failure) {
        deleteFailure(failure)
    }

    private fun deleteFailure(failure: Failure) {
        viewModelScope.launch {
            failureDao.delete(failure)
        }
    }

    private fun insertFailure(failure: Failure) {
        viewModelScope.launch {
            failureDao.insert(failure)
        }
    }

    private fun getNewFailureEntry(panel: String,
                                 task: String,
                                 plant: String,
                                 equipment: String,
                                 value: String): Failure {
        return Failure(
            plant = plant,
            panel = panel,
            equipment = equipment,
            value = value,
            task = task,
            solved = false,
            active = null
        )
    }

    fun retrieveFailureByPlantAndPanel(plant: String, panel: String): LiveData<List<Failure>>  {
        return failureDao.getFailuresByPlantAndPanel(plant, panel).asLiveData()
    }

    fun retrieveFailureByPanel(panel: String): LiveData<List<Failure>>  {
        return failureDao.getFailuresByPanel(panel).asLiveData()
    }

    fun retrieveFailureByPlant(plant: String): LiveData<List<Failure>>  {
        return failureDao.getFailuresByPlant(plant).asLiveData()
    }

    fun retrieveFailureUnsolvedByEquipment(equipment: String): LiveData<Failure>  {
        return failureDao.getFailureUnsolvedByEquipment(equipment).asLiveData()
    }

    fun retrieveFailuresByEquipment(equipment: String): LiveData<List<Failure>>  {
        return failureDao.getFailuresEquipment(equipment).asLiveData()
    }

    fun updateFailure(failure: Failure) {
        update(failure)

    }

    private fun update(failure: Failure) {

        viewModelScope.launch {
            failureDao.updateFailure(failure)
        }
    }
}


class FailuresViewModelFactory(private val failureDao: FailureDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FailuresViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FailuresViewModel(failureDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}