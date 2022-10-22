package ar.edu.ort.jefud_notifying_system.adapter

import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ar.edu.ort.jefud_notifying_system.R
import ar.edu.ort.jefud_notifying_system.model.Alarm
import ar.edu.ort.jefud_notifying_system.model.HistoricAlarm
import kotlinx.coroutines.flow.Flow

class AlarmOperatorViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val textView4 : TextView = itemView.findViewById(R.id.textView4)
    private val textView3 : TextView = itemView.findViewById(R.id.textView3)
    private val textView10 : TextView = itemView.findViewById(R.id.textView10)
    private val textView9 : TextView = itemView.findViewById(R.id.textView9)
    private val textView8 : TextView = itemView.findViewById(R.id.textView8)
    private val textView12 : TextView = itemView.findViewById(R.id.textView12)
    private val progress_bar : ProgressBar = itemView.findViewById(R.id.progress_bar)

    fun bind(alarm: Alarm, historicAlarm: HistoricAlarm) {

        textView4.text = alarm.description + " " + alarm.tagName
        textView3.text = historicAlarm.priority.toString()
        textView10.text = alarm.operatorAction
        textView9.text = alarm.equipment
        textView8.text = alarm.plant
        progress_bar.progress = historicAlarm.value.toInt()
        textView12.text = alarm.max.toString()


    }
}