package ar.edu.ort.jefud_notifying_system.adapter

import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import ar.edu.ort.jefud_notifying_system.R
import ar.edu.ort.jefud_notifying_system.model.Failure



class FailureViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)  {
    private val failureTitleTask : TextView = itemView.findViewById(R.id.failureTitleTask)
    private val failureSubtitleEquip : TextView = itemView.findViewById(R.id.failureSubtitleEquip)


    fun bind(failure: Failure) {
        failureTitleTask.text = failure.task
        failureSubtitleEquip.text = failure.equipment
    }

    fun getCardLayout (): CardView {
        return itemView.findViewById(R.id.failureItem)
    }
}