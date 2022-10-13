package ar.edu.ort.jefud_notifying_system.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ar.edu.ort.jefud_notifying_system.R
import ar.edu.ort.jefud_notifying_system.model.HistoricAlarm

class AlarmRecordViewHolder(alarmView: View): RecyclerView.ViewHolder(alarmView) {

    private val textView14 : TextView = itemView.findViewById(R.id.textView14)
    private val textView15 : TextView = itemView.findViewById(R.id.textView15)

    fun bind(alarm: HistoricAlarm) {
        textView14.text = alarm.tagName
        textView15.text = alarm.datetime
    }
}