package ar.edu.ort.jefud_notifying_system.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ar.edu.ort.jefud_notifying_system.R
import ar.edu.ort.jefud_notifying_system.model.Alarm
import ar.edu.ort.jefud_notifying_system.model.HistoricAlarm
import kotlinx.coroutines.flow.Flow

class AlarmPanelistViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    //private val textView4 : TextView = itemView.findViewById(R.id.textView4)

    fun bind(alarm: Flow<Alarm>, historicAlarm: HistoricAlarm) {
        //textView4.text = alarm.description + " " + alarm.tagName
        //textView3.text = historicAlarm.priority
        //textView10.text = alarm.panelistAction
        //textView9.text = alarm.equipment
        //textView8.text = alarm.plant
        //valueProgressBar = historicAlarm.value.toInt()
        //maxProgressBar = alarm.max
        //minProgressBar = alarm.min
    }
}