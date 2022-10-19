package ar.edu.ort.jefud_notifying_system.view.panelist

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import ar.edu.ort.jefud_notifying_system.R
import ar.edu.ort.jefud_notifying_system.database.JEFUDApplication
import ar.edu.ort.jefud_notifying_system.databinding.FragmentPanelistMessageSendingBinding
import ar.edu.ort.jefud_notifying_system.model.Message
import ar.edu.ort.jefud_notifying_system.viewmodel.MessageViewModel
import ar.edu.ort.jefud_notifying_system.viewmodel.MessageViewModelFactory

class PanelistMessageSending : Fragment() {

    lateinit var btnGoToReceivedMessages : TextView
    lateinit var btnSend : TextView
    private var _binding: FragmentPanelistMessageSendingBinding? = null
    private val binding get() = _binding!!
    lateinit var vista: View
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
        // Inflate the layout for this fragment
        _binding = FragmentPanelistMessageSendingBinding.inflate(inflater, container, false)
        btnGoToReceivedMessages = binding.buttonToReceived
        btnSend = binding.button2

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        btnGoToReceivedMessages.setOnClickListener {
            val action = PanelistMessageSendingDirections.actionPanelistMessageSendingToPanelistMessagesReceived()

            findNavController().navigate(action)
        }
        btnSend.setOnClickListener{
            val userDetails = requireContext().getSharedPreferences("userdetails",
                Context.MODE_PRIVATE
            )
            val userDni = userDetails.getString("dni", "")
            val textWrittenEditText = binding.textWrittenEditText
            val text = textWrittenEditText.text.toString()
            binding.textWrittenEditText.text.clear()
            val dniRecipient = "44852"
            if(userDni != null)
            //dnireciepent: traer al user que coincida en rol y panel
            viewModelMessages.addNewMessage(userDni, dniRecipient, text, false)
        }

    }

}