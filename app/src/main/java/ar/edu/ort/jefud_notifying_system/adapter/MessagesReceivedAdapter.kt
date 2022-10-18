package ar.edu.ort.jefud_notifying_system.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import ar.edu.ort.jefud_notifying_system.R
import ar.edu.ort.jefud_notifying_system.dao.UserDao
import ar.edu.ort.jefud_notifying_system.listener.onItemClickListener
import ar.edu.ort.jefud_notifying_system.model.Message
import ar.edu.ort.jefud_notifying_system.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class MessagesReceivedAdapter(private val messagesList: MutableList<Message>, private val onItemClick: onItemClickListener, private val userDao: UserDao): RecyclerView.Adapter<MessageReceivedViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageReceivedViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.message_item, parent, false)
        return MessageReceivedViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageReceivedViewHolder, position: Int) {
        val user: User
        runBlocking(Dispatchers.IO) {
            user = searchUser(messagesList[position].dniSender).first()
        }
        holder.bind(user)

        holder.getCardLayout().setOnClickListener{
            onItemClick.onViewItemDetail(messagesList[position], user)
        }

    }

    private fun searchUser(dniSender: String): Flow<User> {
        return userDao.getUser(dniSender)
    }

    override fun getItemCount(): Int {
        return messagesList.size
    }
}

