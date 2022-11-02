package ar.edu.ort.jefud_notifying_system.view.operator

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.activityViewModels
import ar.edu.ort.jefud_notifying_system.R
import ar.edu.ort.jefud_notifying_system.database.JEFUDApplication
import ar.edu.ort.jefud_notifying_system.databinding.FragmentMessageDetailsBinding
import ar.edu.ort.jefud_notifying_system.databinding.FragmentOperatorFailureUploadingBinding
import ar.edu.ort.jefud_notifying_system.viewmodel.*
import java.util.*


class OperatorFailureUploading : Fragment() {
    lateinit var btnUploadFailure : Button
    lateinit var equipmentSpinner : Spinner
    lateinit var taskSpinner : Spinner
    lateinit var valueSpinner : Spinner
    private var _binding: FragmentOperatorFailureUploadingBinding? = null
    private val binding get() = _binding!!
    private val viewModelFailures: FailuresViewModel by activityViewModels {
        FailuresViewModelFactory(
            (activity?.application as JEFUDApplication).database
                .failureDao()
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
        _binding = FragmentOperatorFailureUploadingBinding.inflate(inflater, container, false)
        btnUploadFailure = binding.uploadingButton
        equipmentSpinner = binding.equipmentSpinner
        taskSpinner = binding.taskSpinner
        valueSpinner = binding.valueSpinner
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.array_equipment,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            equipmentSpinner.adapter = adapter
        }

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.array_task,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            taskSpinner.adapter = adapter
        }

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.array_values,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            valueSpinner.adapter = adapter
        }

        btnUploadFailure.setOnClickListener{
            val userDetails = requireContext().getSharedPreferences("userdetails",
                Context.MODE_PRIVATE
            )
            val userPanel = userDetails.getString("panel", "")
            val userPlant = userDetails.getString("plant", "")
            val equipment = equipmentSpinner.selectedItem.toString()
            val task = taskSpinner.selectedItem.toString()
            val value = valueSpinner.selectedItem.toString()

            if(userPanel != null && userPlant != null){
                viewModelFailures.addNewFailure(userPanel,task,userPlant,equipment,value)

                Toast.makeText(context, "Desvio agregado", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}