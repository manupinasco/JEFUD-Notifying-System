package ar.edu.ort.jefud_notifying_system.adapter

import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import ar.edu.ort.jefud_notifying_system.R
import ar.edu.ort.jefud_notifying_system.model.User

class MessageHistoricViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    private val messageTitleTextView : TextView = itemView.findViewById(R.id.messageTitleTextView)

    fun bind(user: User) {
        messageTitleTextView.text = user.name + " " + user.surname
    }

    fun getCardLayout (): CardView {
        return itemView.findViewById(R.id.itemMessage)
    }
}