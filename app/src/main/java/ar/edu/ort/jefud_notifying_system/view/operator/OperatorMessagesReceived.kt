package ar.edu.ort.jefud_notifying_system.view.operator

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ar.edu.ort.jefud_notifying_system.R
import ar.edu.ort.jefud_notifying_system.adapter.MessagesReceivedAdapter
import ar.edu.ort.jefud_notifying_system.database.JEFUDApplication
import ar.edu.ort.jefud_notifying_system.databinding.FragmentMessagesReceivedBinding
import ar.edu.ort.jefud_notifying_system.listener.onItemClickListener
import ar.edu.ort.jefud_notifying_system.model.Message
import ar.edu.ort.jefud_notifying_system.model.User
import ar.edu.ort.jefud_notifying_system.view.panelist.PanelistMessagesReceivedDirections
import ar.edu.ort.jefud_notifying_system.viewmodel.MessageViewModel
import ar.edu.ort.jefud_notifying_system.viewmodel.MessageViewModelFactory

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [OperatorMessagesReceived.newInstance] factory method to
 * create an instance of this fragment.
 */
class OperatorMessagesReceived : Fragment(), onItemClickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
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

        addData()

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
            val action = OperatorMessagesReceivedDirections.actionOperatorMessagesReceivedToOperatorMessageSending()
            findNavController().navigate(action)
        }
        btnGoToHistoric.setOnClickListener {
            val action = OperatorMessagesReceivedDirections.actionOperatorMessagesReceivedToOperatorMessageHistoric()
            findNavController().navigate(action)
        }
    }

    private fun addData() {

        viewModelMessages.allMessages.observe(this.viewLifecycleOwner) { messages ->
            if(messages.size == 0) {
                viewModelMessages.addNewMessage("44852", "44", "Estimado panelista, le notifico que habrá una reunión con la gerencia el día de la fecha a las 14:30hs.", false)
            }
        }

    }

    private fun getMessages(messages: List<Message>?) {
        val userDetails = requireContext().getSharedPreferences("userdetails",
            Context.MODE_PRIVATE
        )
        val userDni = userDetails.getString("dni", "")

        if(messages != null && userDni != null)
            for (i in 0..(messages.size-1)) {
                if(messages[i].dniRecipient?.compareTo(userDni) == 0) {
                    messagesList.add(messages[i])
                }
            }
        activity?.runOnUiThread(Runnable { messageListAdapter.notifyDataSetChanged() })

    }

    override fun onViewItemDetail(message: Message, user: User) {
        message.read = true
        viewModelMessages.updateMessage(message)

        findNavController().navigate((OperatorMessagesReceivedDirections.actionOperatorMessagesReceivedToOperatorMessageDetails(user.name, user.surname, message.message, user.role)))
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment OperatorMessagesReceived.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            OperatorMessagesReceived().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}