package ar.edu.ort.jefud_notifying_system.view.manager

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ar.edu.ort.jefud_notifying_system.R
import ar.edu.ort.jefud_notifying_system.database.JEFUDApplication
import ar.edu.ort.jefud_notifying_system.databinding.FragmentManagerFrecuentAlarmBinding
import ar.edu.ort.jefud_notifying_system.databinding.FragmentManagerOosBinding
import ar.edu.ort.jefud_notifying_system.model.Failure
import ar.edu.ort.jefud_notifying_system.model.HistoricAlarm
import ar.edu.ort.jefud_notifying_system.viewmodel.*


class ManagerOos : Fragment() {
    private var _binding: FragmentManagerOosBinding? = null
    private val binding get() = _binding!!
    private val viewModelFailures: FailuresViewModel by activityViewModels {
        FailuresViewModelFactory(
            (activity?.application as JEFUDApplication).database
                .failureDao()
        )
    }
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
        _binding = FragmentManagerOosBinding.inflate(inflater, container, false)



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
            val action = ManagerOosDirections.actionManagerOOSToManagerFrecuentAlarm()
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
        val userDetails = requireContext().getSharedPreferences("userdetails",
            Context.MODE_PRIVATE
        )
        val plant = userDetails.getString("plant", "")
        if(equipment != "" && plant != null)
            if(equipment.compareTo("Todos") == 0) {
                binding.frecuentAlarmAdviseTextView.visibility = View.INVISIBLE
                viewModelFailures.retrieveFailureByPlant(
                        plant
                    ).observe(this.viewLifecycleOwner) { failures ->
                        bindFailuresData(failures)
                    }

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
        val amountFailures = failures.size
        val equipment = binding.spinner.selectedItem.toString()
        binding.frecuentAlarmProgressBar.progress = amountFailures
        if(amountFailures > 2) {
            val frecuentAlarmAdviseTextView = binding.frecuentAlarmAdviseTextView
            frecuentAlarmAdviseTextView.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.alert_color))
            frecuentAlarmAdviseTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_alert_alarm, 0, 0, 0);
            val text = "Gran frecuencia de oos activos en el equipo " + equipment + ". Solucionarlo."
            val ss = SpannableString(text)
            val userDetails = requireContext().getSharedPreferences("userdetails",
                Context.MODE_PRIVATE
            )
            val userDni = userDetails.getString("dni", "")
            val clickableSpan: ClickableSpan = object : ClickableSpan() {
                override fun onClick(textView: View) {

                    viewModelUsers.retrieveUserByPanel(failures[0].panel, "COORDINATOR")
                        .observe(viewLifecycleOwner) { user ->
                            if (userDni != null)
                                viewModelMessages.addNewMessage(
                                    user.dni,
                                    userDni,
                                    ("En vistas de la gran frecuencia de oos activos en el equipo " + equipment + ", pido que se comunique conmigo para gestionar una reunión de revisión."),
                                    false
                                )
                        }

                    Toast.makeText(context, "Mensaje enviado a coordinador", Toast.LENGTH_SHORT)
                        .show()


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