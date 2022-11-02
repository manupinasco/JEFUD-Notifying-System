package ar.edu.ort.jefud_notifying_system.view.coordinator

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ar.edu.ort.jefud_notifying_system.adapter.MessagesReceivedAdapter
import ar.edu.ort.jefud_notifying_system.database.JEFUDApplication
import ar.edu.ort.jefud_notifying_system.databinding.FragmentMessagesReceivedBinding
import ar.edu.ort.jefud_notifying_system.listener.onItemClickListener
import ar.edu.ort.jefud_notifying_system.model.Message
import ar.edu.ort.jefud_notifying_system.model.User
import ar.edu.ort.jefud_notifying_system.viewmodel.MessageViewModel
import ar.edu.ort.jefud_notifying_system.viewmodel.MessageViewModelFactory

class CoordinatorMessagesReceived: Fragment(), onItemClickListener{
    private lateinit var btnGoToSendingMessage : TextView
    private lateinit var btnGoToHistoric : TextView
    private var _binding: FragmentMessagesReceivedBinding? = null
    private val binding get() = _binding!!
    private lateinit var vista: View
    private lateinit var recMessage : RecyclerView
    private var messagesList : MutableList<Message> = ArrayList<Message>()
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var messageListAdapter: MessagesReceivedAdapter
    private val viewModelMessages: MessageViewModel by activityViewModels {
        MessageViewModelFactory(
            (activity?.application as JEFUDApplication).database
                .messageDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMessagesReceivedBinding.inflate(inflater, container, false)
        btnGoToSendingMessage = binding.buttonToSend
        btnGoToHistoric = binding.buttonToHistoric

        viewModelMessages.allMessages.observe(this.viewLifecycleOwner) {messages ->

            getMessages(messages)
        }

        recMessage = binding.messagesReceivedRecyclerView
        recMessage.setHasFixedSize(true)
        linearLayoutManager = LinearLayoutManager(context)

        recMessage.layoutManager = linearLayoutManager

        messageListAdapter = MessagesReceivedAdapter(messagesList, this, (activity?.application as JEFUDApplication).database
            .userDao())

        recMessage.adapter = messageListAdapter

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        // Limpiamos la lista para evitar elementos duplicados
        messagesList.clear()

        btnGoToSendingMessage.setOnClickListener {
            val action = CoordinatorMessagesReceivedDirections.actionCoordinatorMessagesReceivedToCoordinatorMessageSending()
            findNavController().navigate(action)
        }
        btnGoToHistoric.setOnClickListener {
            val action = CoordinatorMessagesReceivedDirections.actionCoordinatorMessagesReceivedToCoordinatorMessageHistoric()
            findNavController().navigate(action)
        }
    }

    private fun getMessages(messages: List<Message>?) {
        val userDetails = requireContext().getSharedPreferences("userdetails",
            Context.MODE_PRIVATE
        )
        val userDni = userDetails.getString("dni", "")

        if(messages != null && userDni != null)
            for (i in messages.indices) {
                if(messages[i].dniRecipient?.compareTo(userDni) == 0) {
                    messagesList.add(messages[i])
                }
            }
        activity?.runOnUiThread(Runnable { messageListAdapter.notifyDataSetChanged() })

    }

    override fun onViewItemDetail(message: Message, user: User) {
        message.read = true
        viewModelMessages.updateMessage(message)

        findNavController().navigate((CoordinatorMessagesReceivedDirections.actionCoordinatorMessagesReceivedToCoordinatorMessageDetails(user.name, user.surname, message.message, user.role)))
    }
}