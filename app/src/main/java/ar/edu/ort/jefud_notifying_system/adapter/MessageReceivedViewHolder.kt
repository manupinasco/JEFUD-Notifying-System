package ar.edu.ort.jefud_notifying_system.adapter

import android.util.Log
import android.view.View
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import ar.edu.ort.jefud_notifying_system.R
import ar.edu.ort.jefud_notifying_system.model.User

class MessageReceivedViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

   private val messageTitleTextView : TextView = itemView.findViewById(R.id.messageTitleTextView)

    private val messageRedPoint : ImageView = itemView.findViewById(R.id.message_red_point)

    fun bind(user: User, read: Boolean) {
        messageTitleTextView.text = user.name + " " + user.surname
        if(!read) {
            messageRedPoint.visibility = VISIBLE
        }
    }

    fun getCardLayout (): CardView {
        return itemView.findViewById(R.id.itemMessage)
    }
}