package ar.edu.ort.jefud_notifying_system.view.coordinator

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
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
import ar.edu.ort.jefud_notifying_system.R
import ar.edu.ort.jefud_notifying_system.database.JEFUDApplication
import ar.edu.ort.jefud_notifying_system.databinding.FragmentCoordinatorFrecuentAlarmBinding
import ar.edu.ort.jefud_notifying_system.model.Alarm
import ar.edu.ort.jefud_notifying_system.viewmodel.*
import java.util.*


class CoordinatorFrecuentAlarm : Fragment() {
    private var _binding: FragmentCoordinatorFrecuentAlarmBinding? = null
    private val binding get() = _binding!!
    private val viewModelHistoricAlarm: HistoricAlarmsViewModel by activityViewModels {
        HistoricAlarmsViewModelFactory(
            (activity?.application as JEFUDApplication).database
                .historicAlarmDao()
        )
    }
    private val viewModelAlarm: AlarmsViewModel by activityViewModels {
        AlarmsViewModelFactory(
            (activity?.application as JEFUDApplication).database
                .alarmDao()
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
        _binding = FragmentCoordinatorFrecuentAlarmBinding.inflate(inflater, container, false)


        val spinner = binding.spinner
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.array_tags,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }












        return binding.root
    }

    override fun onStart() {
        super.onStart()

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

    @SuppressLint("ResourceAsColor")
    private fun bindData(spinner: Spinner) {

        val tag = spinner.selectedItem.toString()
        val userDetails = requireContext().getSharedPreferences(
            "userdetails",
            Context.MODE_PRIVATE
        )
        val panel = userDetails.getString("panel", "")
        if (tag != "" && panel != null)
            viewModelHistoricAlarm.retrieveAlarmsByTagAndMonthAndPanel(
                tag, "10", panel
            ).observe(this.viewLifecycleOwner) { alarms ->
                val amountAlarms = alarms.size
                binding.frecuentAlarmProgressBar.progress = amountAlarms
                if(amountAlarms > 2) {
                    val frecuentAlarmAdviseTextView = binding.frecuentAlarmAdviseTextView
                    frecuentAlarmAdviseTextView.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.alert_color))
                    frecuentAlarmAdviseTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_alert_alarm, 0, 0, 0);
                    val text =
                        "Gran frecuencia de alarmas con tag name" + " " + tag + ". Solucionarlo."
                    val ss = SpannableString(text)
                    val clickableSpan: ClickableSpan = object : ClickableSpan() {
                        override fun onClick(textView: View) {
                            sendSolution(tag)
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
                    frecuentAlarmAdviseTextView.text = "Correcta frecuencia de alarmas"
                    frecuentAlarmAdviseTextView.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.green_color))
                    frecuentAlarmAdviseTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_ok_alarm, 0, 0, 0);
                }
            }
    }

    private fun sendSolution(tag: String) {
        val userDetails = requireContext().getSharedPreferences(
            "userdetails",
            Context.MODE_PRIVATE
        )
        val userDni = userDetails.getString("dni", "")
        val panel = userDetails.getString("panel", "")

            if (panel != null)
            viewModelHistoricAlarm.retrieveAlarmsByTagAndMonthAndPanel(
                tag, "10", panel
            ).observe(this.viewLifecycleOwner) { alarms ->
                var i = 0
                var iobad = false
                while(i < alarms.size && !iobad) {
                    if(alarms[i].type.compareTo("IOBAD") == 0) {
                        iobad = true
                    }
                    i++
                }
                if(iobad) {
                    binding.frecuentAlarmAdviseTextView.text =
                        "Falla crítica en el equipo. Enviar equipo de mantenimiento a repararlo"

                }
                else {
                    var shift = ""
                    var amountAlarmsShiftMorning = 0
                    var amountAlarmsShiftAfternoon = 0
                    var amountAlarmsShiftNight = 0

                    for (alarm in alarms) {
                        when (alarm.datetime.substring(11, 13).toInt()) {

                            in 6..13 -> {
                                amountAlarmsShiftMorning++
                            }
                            in 14..19 -> {
                                amountAlarmsShiftAfternoon++
                            }
                            else -> {
                                amountAlarmsShiftNight++
                            }
                        }


                    }
                    if ((100 * amountAlarmsShiftMorning) / alarms.size >= 50) {
                        shift = "MAÑANA"
                    } else if ((100 * amountAlarmsShiftAfternoon) / alarms.size >= 50) {
                        shift = "TARDE"
                    } else if ((100 * amountAlarmsShiftNight) / alarms.size >= 50) {
                        shift = "NOCHE"
                    }

                    if (shift != "" && panel != null) {
                        Log.e("hola",((100 * amountAlarmsShiftMorning) / alarms.size).toString())
                        viewModelUsers.retrieveUserByPanelAndShift("PANELIST", panel, shift)
                            .observe(this.viewLifecycleOwner) { user ->
                                if (userDni != null)
                                    if(user!= null)
                                    viewModelMessages.addNewMessage(
                                        user.dni,
                                        userDni,

                                        ("En vistas de la gran frecuencia de alarmas de tipo " + tag + " en su turno, pido que se comunique conmigo para gestionar una reunión de revisión."),
                                        false
                                    )
                            }

                        Toast.makeText(context, "Mensaje enviado al panelista", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        binding.frecuentAlarmAdviseTextView.text =
                            "Falla crítica en el equipo. Enviar equipo de mantenimiento a repararlo"

                    }
                }


            }

        }

    }


