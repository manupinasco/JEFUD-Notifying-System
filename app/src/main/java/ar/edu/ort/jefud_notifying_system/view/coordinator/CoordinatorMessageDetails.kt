package ar.edu.ort.jefud_notifying_system.view.coordinator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ar.edu.ort.jefud_notifying_system.databinding.FragmentMessageDetailsBinding

class CoordinatorMessageDetails: Fragment() {
    private var _binding: FragmentMessageDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var btnReturn : Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMessageDetailsBinding.inflate(inflater, container, false)
        binding.nameMessageDetailsTextView.text = CoordinatorMessageDetailsArgs.fromBundle(requireArguments()).name + " " + CoordinatorMessageDetailsArgs.fromBundle(requireArguments()).surname
        binding.rolMessageDetails.text = CoordinatorMessageDetailsArgs.fromBundle(requireArguments()).role
        binding.messageTextTextView.text = CoordinatorMessageDetailsArgs.fromBundle(requireArguments()).message
        btnReturn = binding.buttonDetailsToSend
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        btnReturn.setOnClickListener{
            val action = CoordinatorMessageDetailsDirections.actionCoordinatorMessageDetailsToCoordinatorMessagesReceived()
            findNavController().navigate(action)
        }
    }
}