package ar.edu.ort.jefud_notifying_system.view.panelist

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.compose.ui.text.toUpperCase
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import ar.edu.ort.jefud_notifying_system.R
import ar.edu.ort.jefud_notifying_system.database.JEFUDApplication
import ar.edu.ort.jefud_notifying_system.databinding.FragmentMessageSendingBinding
import ar.edu.ort.jefud_notifying_system.model.Message
import ar.edu.ort.jefud_notifying_system.viewmodel.MessageViewModel
import ar.edu.ort.jefud_notifying_system.viewmodel.MessageViewModelFactory
import ar.edu.ort.jefud_notifying_system.viewmodel.UsersViewModel
import ar.edu.ort.jefud_notifying_system.viewmodel.UsersViewModelFactory
import java.util.*

class PanelistMessageSending : Fragment() {

    lateinit var btnGoToReceivedMessages : TextView
    lateinit var btnGoToHistoric : TextView
    lateinit var btnSend : TextView
    private var _binding: FragmentMessageSendingBinding? = null
    private val binding get() = _binding!!
    private val viewModelMessages: MessageViewModel by activityViewModels {
        MessageViewModelFactory(
            (activity?.application as JEFUDApplication).database
                .messageDao()
        )
    }
    private val viewModelUsers: UsersViewModel by activityViewModels {
        UsersViewModelFactory(
            (activity?.application as JEFUDApplication).database
                .userDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMessageSendingBinding.inflate(inflater, container, false)
        btnGoToReceivedMessages = binding.buttonToReceived
        btnGoToHistoric = binding.buttonToHistoric
        btnSend = binding.buttonToSend

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        btnGoToReceivedMessages.setOnClickListener {
            val action = PanelistMessageSendingDirections.actionPanelistMessageSendingToPanelistMessagesReceived()

            findNavController().navigate(action)
        }

        btnGoToHistoric.setOnClickListener {
            val action = PanelistMessageSendingDirections.actionPanelistMessageSendingToPanelistMessageHistoric()

            findNavController().navigate(action)
        }
        val spinner = binding.roleSpinner
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.message_roles_for_panelist,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
        btnSend.setOnClickListener{
            val userDetails = requireContext().getSharedPreferences("userdetails",
                Context.MODE_PRIVATE
            )

            val userDni = userDetails.getString("dni", "")
            val userPanel = userDetails.getString("panel", "")
            val userPlant = userDetails.getString("plant", "")
            val textWrittenEditText = binding.textWrittenEditText
            val text = textWrittenEditText.text.toString()
            binding.textWrittenEditText.text.clear()
            val role = binding.roleSpinner.selectedItem.toString().uppercase(Locale.ROOT)
            var dniRecipient = ""
            if(userDni != null)
            if(role.compareTo("MANAGER") == 0 && userPlant != null) {
                viewModelUsers.retrieveUser(role, userPlant).observe(this.viewLifecycleOwner) {user ->
                    dniRecipient = user.dni
                    viewModelMessages.addNewMessage(userDni, dniRecipient, text, false)
                }
            }
            else {
                viewModelUsers.allUsers.observe(this.viewLifecycleOwner) { users ->
                    for(i in users.indices) {
                        if (userPlant != null && userPanel != null)
                        if(users[i].role.compareTo(role) == 0 && users[i].plant.compareTo(userPlant) == 0 && users[i].panel?.compareTo(userPanel) == 0) {
                            dniRecipient = users[i].dni
                            viewModelMessages.addNewMessage(dniRecipient, userDni, text, false)

                        }
                    }


                }
            }

            Toast.makeText(getContext(), "Mensaje enviado", Toast.LENGTH_SHORT)
                .show()

        }

    }

}