package ar.edu.ort.jefud_notifying_system.view.coordinator

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ar.edu.ort.jefud_notifying_system.R
import ar.edu.ort.jefud_notifying_system.database.JEFUDApplication
import ar.edu.ort.jefud_notifying_system.databinding.FragmentCoordinatorFrecuentAlarmBinding
import ar.edu.ort.jefud_notifying_system.databinding.FragmentCoordinatorOosBinding
import ar.edu.ort.jefud_notifying_system.model.Failure
import ar.edu.ort.jefud_notifying_system.model.HistoricAlarm
import ar.edu.ort.jefud_notifying_system.viewmodel.*

class CoordinatorOos: Fragment() {
    private var _binding: FragmentCoordinatorOosBinding? = null
    private val binding get() = _binding!!
    private val viewModelFailures: FailuresViewModel by activityViewModels {
        FailuresViewModelFactory(
            (activity?.application as JEFUDApplication).database
                .failureDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCoordinatorOosBinding.inflate(inflater, container, false)


        val spinner = binding.spinner
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.array_equipment,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }












        return binding.root
    }

    override fun onStart() {
        super.onStart()

        binding.buttonToFrecuentAlarm.setOnClickListener{
            val action = CoordinatorOosDirections.actionCoordinatorOosToCoordinatorFrecuentAlarm()
            findNavController().navigate(action)
        }
        val spinner = binding.spinner

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                bindData(spinner)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

    }


    private fun bindData(spinner: Spinner) {

        val equipment = spinner.selectedItem.toString()
        val userDetails = requireContext().getSharedPreferences(
            "userdetails",
            Context.MODE_PRIVATE
        )
        val panel = userDetails.getString("panel", "")
        if (equipment != "" && panel != null)
            if(equipment.compareTo("Todos") == 0) {
                viewModelFailures.retrieveFailureByPanel(
                    panel
                ).observe(this.viewLifecycleOwner) { failures ->
                    bindFailuresData(failures)
                }
                binding.frecuentAlarmAdviseTextView.visibility = View.INVISIBLE
            }
            else {
                binding.frecuentAlarmAdviseTextView.visibility = View.VISIBLE
                viewModelFailures.retrieveFailuresByEquipment(
                    equipment
                ).observe(this.viewLifecycleOwner) { failures ->
                    bindFailuresData(failures)
                }

            }
    }

    @SuppressLint("ResourceAsColor")
    private fun bindFailuresData(failures: List<Failure>) {
        val equipment = binding.spinner.selectedItem.toString()
        val amountFailures = failures.size
        binding.frecuentAlarmProgressBar.progress = amountFailures
        if(amountFailures > 2) {
            val frecuentAlarmAdviseTextView = binding.frecuentAlarmAdviseTextView
            frecuentAlarmAdviseTextView.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.alert_color))
            frecuentAlarmAdviseTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_alert_alarm, 0, 0, 0);
            val text =
                "Gran frecuencia de oos en el equipo " + equipment + ". Solucionarlo."
            val ss = SpannableString(text)

            val tag = binding.spinner.selectedItem.toString()
            val clickableSpan: ClickableSpan = object : ClickableSpan() {
                override fun onClick(textView: View) {
                    binding.frecuentAlarmAdviseTextView.text =
                        "Falla crítica en el equipo. Enviar equipo de mantenimiento a repararlo"
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.color = ds.linkColor
                    ds.isUnderlineText = true
                }
            }
            ss.setSpan(
                clickableSpan,
                text.length - 13,
                text.length,
                Spanned.SPAN_EXCLUSIVE_INCLUSIVE
            )

            frecuentAlarmAdviseTextView.text = ss
            frecuentAlarmAdviseTextView.movementMethod = LinkMovementMethod.getInstance()
            frecuentAlarmAdviseTextView.highlightColor = android.R.color.transparent
        }
        else {

            val frecuentAlarmAdviseTextView = binding.frecuentAlarmAdviseTextView
            frecuentAlarmAdviseTextView.text = "Correcta frecuencia de oos"
            frecuentAlarmAdviseTextView.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.green_color))
            frecuentAlarmAdviseTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_ok_alarm, 0, 0, 0);
        }
    }
}