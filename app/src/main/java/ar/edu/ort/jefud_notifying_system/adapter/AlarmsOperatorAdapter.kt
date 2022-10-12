package ar.edu.ort.jefud_notifying_system.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ar.edu.ort.jefud_notifying_system.R
import ar.edu.ort.jefud_notifying_system.dao.AlarmDao
import ar.edu.ort.jefud_notifying_system.model.Alarm
import ar.edu.ort.jefud_notifying_system.model.HistoricAlarm
import kotlinx.coroutines.flow.Flow

class AlarmsOperatorAdapter(private val alarmList: List<HistoricAlarm>, private val alarmDao: AlarmDao): RecyclerView.Adapter<AlarmPanelistViewHolder>() {

    private fun searchAlarm(tagName: String): Flow<Alarm> {
        return alarmDao.getAlarmByTag(tagName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmPanelistViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.alarm_item, parent, false)
        return AlarmPanelistViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlarmPanelistViewHolder, position: Int) {
        val alarm = searchAlarm(alarmList[position].tagName)
        holder.bind(alarm, alarmList[position])
    }

    override fun getItemCount(): Int {
        return alarmList.size
    }
}