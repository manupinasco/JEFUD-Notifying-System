package ar.edu.ort.jefud_notifying_system.view.panelist

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.app.NotificationCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ar.edu.ort.jefud_notifying_system.R
import ar.edu.ort.jefud_notifying_system.adapter.MessagesReceivedAdapter
import ar.edu.ort.jefud_notifying_system.database.JEFUDApplication
import ar.edu.ort.jefud_notifying_system.databinding.FragmentPanelistMessageSendingBinding
import ar.edu.ort.jefud_notifying_system.databinding.FragmentPanelistMessagesReceivedBinding
import ar.edu.ort.jefud_notifying_system.listener.onItemClickListener
import ar.edu.ort.jefud_notifying_system.model.Message
import ar.edu.ort.jefud_notifying_system.model.User
import ar.edu.ort.jefud_notifying_system.viewmodel.MessageViewModel
import ar.edu.ort.jefud_notifying_system.viewmodel.MessageViewModelFactory

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PanelistMessage.newInstance] factory method to
 * create an instance of this fragment.
 */
class PanelistMessage : Fragment(), onItemClickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var btnGoToSendingMessage : Button
    private lateinit var btnGoToMessage : Button
    private var _binding: FragmentPanelistMessagesReceivedBinding? = null
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPanelistMessagesReceivedBinding.inflate(inflater, container, false)
        btnGoToSendingMessage = binding.button3
        btnGoToMessage = binding.button3
        vista = inflater.inflate(R.layout.fragment_panelist_messages_received, container, false)
        viewModelMessages.allMessages.observe(this.viewLifecycleOwner) {messages ->

            getMessages(messages)
        }

        recMessage = vista.findViewById(R.id.messages)
        recMessage.setHasFixedSize(true)
        linearLayoutManager = LinearLayoutManager(context)

        recMessage.layoutManager = linearLayoutManager

        messageListAdapter = MessagesReceivedAdapter(messagesList, this)

        recMessage.adapter = messageListAdapter

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        btnGoToSendingMessage.setOnClickListener {
            val action = PanelistMessageDirections.actionPanelistMessageFrToPanelistMessageSending()
            vista.findNavController().navigate(action)
        }

        btnGoToMessage.setOnClickListener{

        }

        // Create an explicit intent for an Activity in your app


    }

    private fun getMessages(messages: List<Message>?) {
        val userDetails = requireContext().getSharedPreferences("userdetails",
            Context.MODE_PRIVATE
        )
        val userDni = userDetails.getString("dni", "")

        if(messages != null && userDni != null)
            for (i in 0..(messages.size-1)) {
                if(messages[i].dniRecipient.compareTo(userDni) == 0) {
                    messagesList.add(messages[i])
                }
            }

    }

    override fun onViewItemDetail(message: Message, user: User) {
        message.read = true
        viewModelMessages.updateMessage(message)

        findNavController().navigate((PanelistMessageDirections.actionPanelistMessageFrToPanelistMessageDetails(message, user)))
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PanelistMessage.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PanelistMessage().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}