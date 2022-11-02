package ar.edu.ort.jefud_notifying_system.view.coordinator

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ar.edu.ort.jefud_notifying_system.R
import ar.edu.ort.jefud_notifying_system.database.JEFUDApplication
import ar.edu.ort.jefud_notifying_system.databinding.FragmentFailureDetailsBinding
import ar.edu.ort.jefud_notifying_system.databinding.FragmentMessageDetailsBinding
import ar.edu.ort.jefud_notifying_system.viewmodel.FailuresViewModel
import ar.edu.ort.jefud_notifying_system.viewmodel.FailuresViewModelFactory

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CoordinatorFailureDetails.newInstance] factory method to
 * create an instance of this fragment.
 */
class CoordinatorFailureDetails : Fragment() {
    private var _binding: FragmentFailureDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var btnReturn : Button
    private lateinit var btnRewrite : Button
    private lateinit var btnSolve : Button
    private val viewModelFailure: FailuresViewModel by activityViewModels {
        FailuresViewModelFactory(
            (activity?.application as JEFUDApplication).database
                .failureDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFailureDetailsBinding.inflate(inflater, container, false)
        binding.equipment.text = CoordinatorFailureDetailsArgs.fromBundle(requireArguments()).equipment
        binding.task.text = CoordinatorFailureDetailsArgs.fromBundle(requireArguments()).task
        binding.value.text = CoordinatorFailureDetailsArgs.fromBundle(requireArguments()).value
        btnReturn = binding.goBack
        btnSolve = binding.solve

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        btnReturn.setOnClickListener{
            goBack()

        }

        /*btnRewrite.setOnClickListener{
            viewModelFailure.retrieveFailureUnsolvedByEquipment(CoordinatorFailureDetailsArgs.fromBundle(requireArguments()).equipment).observe(this.viewLifecycleOwner) {
                failure ->
                failure.solved = true
                viewModelFailure.updateFailure(failure)
            }
            goBack()
        }*/

        btnSolve.setOnClickListener{

            viewModelFailure.retrieveFailureUnsolvedByEquipment(CoordinatorFailureDetailsArgs.fromBundle(requireArguments()).equipment).observe(this.viewLifecycleOwner) {
                    failure ->
                failure.solved = true
                viewModelFailure.updateFailure(failure)
                goBack()
            }


        }

    }

    private fun goBack() {
        val action = CoordinatorFailureDetailsDirections.actionCoordinatorFailureDetailsToCoordinatorFailures()
        findNavController().navigate(action)
    }

}