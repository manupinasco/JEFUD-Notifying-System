package ar.edu.ort.jefud_notifying_system.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ar.edu.ort.jefud_notifying_system.R
import ar.edu.ort.jefud_notifying_system.model.HistoricAlarm

class AlarmsRecordAdapter(private val alarmList: List<HistoricAlarm>): RecyclerView.Adapter<AlarmRecordViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmRecordViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.alarm_record_item, parent, false)
        return AlarmRecordViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlarmRecordViewHolder, position: Int) {
        holder.bind(alarmList[position])
    }

    override fun getItemCount(): Int {
        return alarmList.size
    }
}