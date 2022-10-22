package ar.edu.ort.jefud_notifying_system.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.RecyclerView
import ar.edu.ort.jefud_notifying_system.R
import ar.edu.ort.jefud_notifying_system.dao.AlarmDao
import ar.edu.ort.jefud_notifying_system.model.Alarm
import ar.edu.ort.jefud_notifying_system.model.HistoricAlarm
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class AlarmsOperatorAdapter(private val alarmList: List<HistoricAlarm>, private val alarmDao: AlarmDao): RecyclerView.Adapter<AlarmOperatorViewHolder>() {

    private fun searchAlarm(tagName: String): Flow<Alarm> {
        return alarmDao.getAlarmByTag(tagName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmOperatorViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.alarm_item, parent, false)
        return AlarmOperatorViewHolder(view)
    }


    override fun getItemCount(): Int {
        return alarmList.size
    }

    override fun onBindViewHolder(holder: AlarmOperatorViewHolder, position: Int) {
        val alarm: Alarm
        runBlocking(Dispatchers.IO) {
            alarm = searchAlarm(alarmList[position].tagName).first()
        }


        holder.bind(alarm, alarmList[position])
    }
}