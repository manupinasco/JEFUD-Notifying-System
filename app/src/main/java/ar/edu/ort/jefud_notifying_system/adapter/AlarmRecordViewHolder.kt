package ar.edu.ort.jefud_notifying_system.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ar.edu.ort.jefud_notifying_system.model.HistoricAlarm

class AlarmRecordViewHolder(alarmView: View): RecyclerView.ViewHolder(alarmView) {

    fun bind(alarm: HistoricAlarm) {
        //name.text = alarm.description
        //datetime.text = alarm.datetime
    }
}