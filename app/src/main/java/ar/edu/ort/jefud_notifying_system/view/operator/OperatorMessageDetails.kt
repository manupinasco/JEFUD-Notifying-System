package ar.edu.ort.jefud_notifying_system.view.operator

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import ar.edu.ort.jefud_notifying_system.R
import ar.edu.ort.jefud_notifying_system.databinding.FragmentMessageDetailsBinding


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [OperatorMessageDetails.newInstance] factory method to
 * create an instance of this fragment.
 */
class OperatorMessageDetails : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentMessageDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var btnReturn : Button


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMessageDetailsBinding.inflate(inflater, container, false)
        binding.textView19.text = OperatorMessageDetailsArgs.fromBundle(requireArguments()).name + " " + OperatorMessageDetailsArgs.fromBundle(requireArguments()).surname + " " + OperatorMessageDetailsArgs.fromBundle(requireArguments()).role

        binding.textView21.text = OperatorMessageDetailsArgs.fromBundle(requireArguments()).message
        btnReturn = binding.buttonDetailsToSend
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        btnReturn.setOnClickListener{
            val action = OperatorMessageDetailsDirections.actionOperatorMessageDetailsToOperatorMessagesReceived()
            findNavController().navigate(action)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment OperatorMessageDetails.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            OperatorMessageDetails().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}