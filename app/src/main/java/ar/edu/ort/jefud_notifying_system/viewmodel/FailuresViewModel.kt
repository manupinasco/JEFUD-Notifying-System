package ar.edu.ort.jefud_notifying_system.viewmodel

import androidx.lifecycle.*

import ar.edu.ort.jefud_notifying_system.dao.FailureDao

import ar.edu.ort.jefud_notifying_system.model.Failure
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
            solved = false
        )
    }

    fun retrieveFailureByPlantAndPanel(plant: String, panel: String): LiveData<List<Failure>>  {
        return failureDao.getFailuresByPlantAndPanel(plant, panel).asLiveData()
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