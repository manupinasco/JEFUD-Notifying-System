package ar.edu.ort.jefud_notifying_system.adapter

import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import ar.edu.ort.jefud_notifying_system.R
import ar.edu.ort.jefud_notifying_system.model.User

class MessageReceivedViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

   // private val textView4 : TextView = itemView.findViewById(R.id.textView4)


    fun bind(user: User) {
        //textView4.text = user.name + " " + user.surname


    }

    fun getCardLayout (): CardView {
        return itemView.findViewById(R.id.card_package_item)
    }
}