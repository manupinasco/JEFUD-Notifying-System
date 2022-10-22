package ar.edu.ort.jefud_notifying_system.viewmodel


import androidx.lifecycle.*
import ar.edu.ort.jefud_notifying_system.dao.AlarmDao
import ar.edu.ort.jefud_notifying_system.model.Alarm
import kotlinx.coroutines.launch

class AlarmsViewModel(private val alarmDao: AlarmDao): ViewModel() {
    val allAlarms: LiveData<List<Alarm>> = alarmDao.getAll().asLiveData()

    fun addNewAlarm(nameVariable: String,
                   description: String,
                   panel: String,
                   tagName: String,
                   textName: String,
                   plant: String,
                   equipment: String,
                   min: Int,
                   max: Int,
                   panelistAction: String,
                   operatorAction: String, infoExtra: String?) {
        val newAlarm = getNewAlarmEntry(nameVariable,
            description,
            panel,
            tagName,
            textName,
            plant,
            equipment,
            min,
            max,
            panelistAction,
            operatorAction, infoExtra)
        insertAlarm(newAlarm)
    }

    fun delete(alarm: Alarm) {
        deleteAlarm(alarm)
    }

    private fun deleteAlarm(alarm: Alarm) {
        viewModelScope.launch {
            alarmDao.delete(alarm)
        }
    }

    private fun insertAlarm(alarm: Alarm) {
        viewModelScope.launch {
            alarmDao.insert(alarm)
        }
    }

    private fun getNewAlarmEntry(nameVariable: String,
                                description: String,
                                panel: String,
                                tagName: String,
                                textName: String,
                                plant: String,
                                equipment: String,
                                min: Int,
                                max: Int,
                                panelistAction: String,
                                operatorAction: String, infoExtra: String?): Alarm {
        return Alarm(
            nameVariable=nameVariable,
            description=description,
            panel=panel,
            tagName=tagName,
            textName=textName,
            plant=plant,
            equipment=equipment,
            min=min,
            max=max,
            panelistAction=panelistAction,
            operatorAction=operatorAction,
            infoExtra = infoExtra
        )
    }

    fun retrieveAlarmByPlant(plant: String): LiveData<Alarm> {
        return alarmDao.getAlarmByPlant(plant).asLiveData()
    }
    fun retrieveAlarmByEquipment(equipment: String): LiveData<Alarm> {
        return alarmDao.getAlarmByEquipment(equipment).asLiveData()
    }
    fun retrieveAlarmByTag(tag: String): LiveData<Alarm> {
        return alarmDao.getAlarmByTag(tag).asLiveData()
    }
}


class AlarmsViewModelFactory(private val alarmDao: AlarmDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AlarmsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AlarmsViewModel(alarmDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}