package ar.edu.ort.jefud_notifying_system.view.manager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import ar.edu.ort.jefud_notifying_system.databinding.FragmentMessageDetailsBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ManagerMessageDetails.newInstance] factory method to
 * create an instance of this fragment.
 */
class ManagerMessageDetails : Fragment() {
    private var _binding: FragmentMessageDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var btnReturn : Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMessageDetailsBinding.inflate(inflater, container, false)
        binding.nameMessageDetailsTextView.text = ManagerMessageDetailsArgs.fromBundle(requireArguments()).name + " " + ManagerMessageDetailsArgs.fromBundle(requireArguments()).surname
        binding.rolMessageDetails.text = ManagerMessageDetailsArgs.fromBundle(requireArguments()).role
        binding.messageTextTextView.text = ManagerMessageDetailsArgs.fromBundle(requireArguments()).message
        btnReturn = binding.buttonDetailsToSend
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        btnReturn.setOnClickListener{
            val action = ManagerMessageDetailsDirections.actionManagerMessageDetailsToManagerMessagesReceived()
            findNavController().navigate(action)
        }
    }
}