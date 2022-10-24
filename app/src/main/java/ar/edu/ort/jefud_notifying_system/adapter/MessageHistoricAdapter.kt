package ar.edu.ort.jefud_notifying_system.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
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


class MessageHistoricAdapter(private val messagesList: MutableList<Message>, private val onItemClick: onItemClickListener, private val userDao: UserDao): RecyclerView.Adapter<MessageHistoricViewHolder>(){
    /** Inflo la vista que recibe el ViewHolder*/
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageHistoricViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.message_item, parent, false)
        return MessageHistoricViewHolder(view)
    }
    /** El return de onCreateViewHolder lo recibo por parametro. holder: Message
     *  Uso .dniRecipient porque quiero inflar el view holder con la persona a quien envié un mensaje*/
    override fun onBindViewHolder(holder: MessageHistoricViewHolder, position: Int) {
        val user: User
        runBlocking(Dispatchers.IO) {
            user = searchUser(messagesList[position].dniRecipient).first()
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