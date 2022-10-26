package ar.edu.ort.jefud_notifying_system.view.manager

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
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import ar.edu.ort.jefud_notifying_system.R
import ar.edu.ort.jefud_notifying_system.database.JEFUDApplication
import ar.edu.ort.jefud_notifying_system.databinding.FragmentManagerFrecuentAlarmBinding
import ar.edu.ort.jefud_notifying_system.viewmodel.*
import java.util.*


class ManagerAlarm : Fragment() {

    private var _binding: FragmentManagerFrecuentAlarmBinding? = null
    private val binding get() = _binding!!
    private val viewModelHistoricAlarm: HistoricAlarmsViewModel by activityViewModels {
        HistoricAlarmsViewModelFactory(
            (activity?.application as JEFUDApplication).database
                .historicAlarmDao()
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
    private val viewModelAlarm: AlarmsViewModel by activityViewModels {
        AlarmsViewModelFactory(
            (activity?.application as JEFUDApplication).database
                .alarmDao()
        )
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentManagerFrecuentAlarmBinding.inflate(inflater, container, false)



        val spinner = binding.spinner
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.array_tags,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        val spinner2 = binding.spinner2
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.array_panels,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner2.adapter = adapter
        }


        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                bindData(spinner, spinner2)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                bindData(spinner, spinner2)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }









        return binding.root
    }

    @SuppressLint("ResourceAsColor")
    private fun bindData(spinner: Spinner, spinner2: Spinner) {
        val tag = spinner.selectedItem.toString()
        val panel = spinner2.selectedItem.toString()
        val userDetails = requireContext().getSharedPreferences("userdetails",
            Context.MODE_PRIVATE
        )
        val userDni = userDetails.getString("dni", "")
        if(tag != "" && panel != "")
        viewModelHistoricAlarm.retrieveAlarmsByTagAndMonth(tag, Calendar.getInstance().get(Calendar.MONTH).toString()).observe(this.viewLifecycleOwner){
                alarms ->
            val amountAlarms = alarms.size
            val text = "Gran frecuencia de alarmas con tag name" + " " + tag + ". Solucionarlo."
            val ss = SpannableString(text)
            val clickableSpan: ClickableSpan = object : ClickableSpan() {
                override fun onClick(textView: View) {

                    viewModelUsers.retrieveUserByPanel(panel, "COORDINATOR").observe(viewLifecycleOwner) {user ->
                        if (userDni != null)
                            viewModelMessages.addNewMessage(userDni, user.dni, ("En vistas de la gran frecuencia de alarmas de tipo " + tag + ", pido que se comunique conmigo para gestionar una reunión de revisión."), false)
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
            ss.setSpan(clickableSpan, text.length - 13, text.length, Spanned.SPAN_EXCLUSIVE_INCLUSIVE)
            val frecuentAlarmAdviseTextView = binding.frecuentAlarmAdviseTextView
            frecuentAlarmAdviseTextView.text = ss
            frecuentAlarmAdviseTextView.movementMethod = LinkMovementMethod.getInstance()
            frecuentAlarmAdviseTextView.highlightColor = android.R.color.transparent
        }
    }



}