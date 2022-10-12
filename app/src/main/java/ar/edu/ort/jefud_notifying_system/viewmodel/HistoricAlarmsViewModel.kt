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
                    priority: Int, datetime: String) {
        val newAlarm = getNewAlarmEntry(tagName, value, priority, datetime)
        insertAlarm(newAlarm)
    }

    private fun insertAlarm(alarm: HistoricAlarm) {
        viewModelScope.launch {
            historicAlarmDao.insert(alarm)
        }
    }

    private fun getNewAlarmEntry(tagName: String,
                                 value: String,
                                 priority: Int, datetime: String): HistoricAlarm {
        return HistoricAlarm(
            value = value,
            priority = priority,
            tagName=tagName,
            datetime = datetime
        )
    }

    fun retrieveAlarmByTag(tag: String): LiveData<HistoricAlarm> {
        return historicAlarmDao.getAlarmByTag(tag).asLiveData()
    }
}


class HistoricAlarmsViewModelFactory(private val historicAlarmDao: HistoricAlarmDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UsersViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HistoricAlarmsViewModel(historicAlarmDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}