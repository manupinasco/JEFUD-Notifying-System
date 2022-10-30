package ar.edu.ort.jefud_notifying_system.viewmodel

import androidx.lifecycle.*
import ar.edu.ort.jefud_notifying_system.dao.AlarmDao
import ar.edu.ort.jefud_notifying_system.dao.HistoricAlarmDao
import ar.edu.ort.jefud_notifying_system.model.Alarm
import ar.edu.ort.jefud_notifying_system.model.HistoricAlarm
import kotlinx.coroutines.launch

class HistoricAlarmsViewModel(private val historicAlarmDao: HistoricAlarmDao): ViewModel() {
    val allAlarms: LiveData<List<HistoricAlarm>> = historicAlarmDao.getAll().asLiveData()

    fun addNewAlarm(tagName: String,
                    value: String,
                    priority: Int, datetime: String, panel: String, plant: String) {
        val newAlarm = getNewAlarmEntry(tagName, value, priority, datetime, panel, plant)
        insertAlarm(newAlarm)
    }

    private fun insertAlarm(alarm: HistoricAlarm) {
        viewModelScope.launch {
            historicAlarmDao.insert(alarm)
        }
    }

    private fun getNewAlarmEntry(tagName: String,
                                 value: String,
                                 priority: Int, datetime: String, panel: String, plant: String): HistoricAlarm {
        return HistoricAlarm(
            value = value,
            priority = priority,
            tagName=tagName,
            datetime = datetime,
            plant = plant,
            panel = panel
        )
    }

    fun retrieveAlarmsByTagAndMonthAndPanel(tag: String, month: String, panel: String): LiveData<List<HistoricAlarm>> {
        return historicAlarmDao.getAlarmsByTagAndMonthAndPanel(tag, month, panel).asLiveData()
    }

    fun retrieveAlarmsByTagAndMonthAndPlant(tag: String, month: String, plant: String): LiveData<List<HistoricAlarm>> {
        return historicAlarmDao.getAlarmsByTagAndMonthAndPlant(tag, month, plant).asLiveData()
    }

    fun retrieveAlarmsByTagAndMonth(tag: String, month: String): LiveData<List<HistoricAlarm>> {
        return historicAlarmDao.getAlarmsByTagAndMonth(tag, month).asLiveData()
    }


    fun delete(historicAlarm: HistoricAlarm) {
        deleteHistoricAlarm(historicAlarm)
    }

    private fun deleteHistoricAlarm(historicAlarm: HistoricAlarm) {
        viewModelScope.launch {
            historicAlarmDao.delete(historicAlarm)
        }
    }
}


class HistoricAlarmsViewModelFactory(private val historicAlarmDao: HistoricAlarmDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistoricAlarmsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HistoricAlarmsViewModel(historicAlarmDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}