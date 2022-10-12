package ar.edu.ort.jefud_notifying_system.view.operator

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ar.edu.ort.jefud_notifying_system.R
import ar.edu.ort.jefud_notifying_system.adapter.AlarmsOperatorAdapter
import ar.edu.ort.jefud_notifying_system.adapter.AlarmsRecordAdapter
import ar.edu.ort.jefud_notifying_system.database.JEFUDApplication
import ar.edu.ort.jefud_notifying_system.databinding.FragmentLoginBinding
import ar.edu.ort.jefud_notifying_system.model.HistoricAlarm
import ar.edu.ort.jefud_notifying_system.view.MainActivity
import ar.edu.ort.jefud_notifying_system.view.panelist.PanelistActivity
import ar.edu.ort.jefud_notifying_system.viewmodel.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [OperatorAlarm.newInstance] factory method to
 * create an instance of this fragment.
 */
class OperatorAlarm : Fragment() {
    // TODO: Rename and change types of parameters
    lateinit var recAlarm : RecyclerView
    lateinit var recAlarmRecord : RecyclerView

    var alarmsList : MutableList<HistoricAlarm> = ArrayList<HistoricAlarm>()
    var alarmsRecord : MutableList<HistoricAlarm> = ArrayList<HistoricAlarm>()

    private lateinit var linearLayoutManager: LinearLayoutManager

    private lateinit var alarmListAdapter: AlarmsOperatorAdapter
    private lateinit var alarmRecordListAdapter: AlarmsRecordAdapter
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentLoginBinding? = null
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        addData()

        viewModelHistoricAlarm.allAlarms.observe(this.viewLifecycleOwner) { alarms ->
            getAlarms(alarms)
        }




        recAlarm.setHasFixedSize(true)
        linearLayoutManager = LinearLayoutManager(context)

        recAlarm.layoutManager = linearLayoutManager


        alarmListAdapter = AlarmsOperatorAdapter(alarmsList, (activity?.application as JEFUDApplication).database
            .alarmDao())

        recAlarm.adapter = alarmListAdapter

        recAlarmRecord.setHasFixedSize(true)
        linearLayoutManager = LinearLayoutManager(context)

        recAlarmRecord.layoutManager = linearLayoutManager


        alarmRecordListAdapter = AlarmsRecordAdapter(alarmsRecord)

        recAlarmRecord.adapter = alarmRecordListAdapter

        //binding.buttonLogout.setOnClickListener { logout() }
    }

    private fun logout() {
        val sharedPrefDni = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with (sharedPrefDni.edit()) {
            putString(getString(R.string.saved_userdni_key),"dni")
            apply()

        }

        val sharedPrefPanel = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with (sharedPrefPanel.edit()) {
            putString(getString(R.string.saved_userpanel_key), "panel")
            apply()

        }

        val sharedPrefPassword = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with (sharedPrefPassword.edit()) {
            putString(getString(R.string.saved_userpassword_key), "password")
            apply()
        }

        val sharedPrefRole = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with (sharedPrefRole.edit()) {
            putString(getString(R.string.saved_userrole_key), "role")
            apply()
        }
        startActivity(Intent(requireContext(), MainActivity::class.java))
    }

    private fun getAlarms(alarms: List<HistoricAlarm>?) {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        val userPanel = sharedPref.getString(getString(R.string.saved_userpanel_key), "")
        if(alarms != null)
        for (i in 0..alarms.size) {
            viewModelAlarm.retrieveAlarmByTag(alarms[i].tagName).observe(this.viewLifecycleOwner) { alarm ->
                if(userPanel?.let { alarm.panel.compareTo(it) } == 0) {
                    if(alarms[i].value.compareTo("rtn") == 0) {
                        if(alarmsRecord.size < 3) {
                            alarmsRecord.add(alarms[i])
                        }
                    }
                    else {
                        alarmsList.add(alarms[i])
                    }
                }
            }


        }

    }

    private fun addData() {
        viewModelAlarm.addNewAlarm("04PA443.1", "ROTURA SELLO P-456", "CCU", "PSG1_GE:04PA443CIN", "ROTURA DE SELLO P-456", "ADIP1", "P-456", 0, 100, "Pedir al operador externo que verifique el estado de la bomba.", "Verificar el estado de la bomba. Si se rompió el sello; detener la bomba/bloquearla y marchar la auxiliar; de lo contrario; la alarma se dispara por rotura del presostato (pedir reparación del mismo y controlar rutinariamente la indicación del manómetro local).")
        viewModelAlarm.addNewAlarm("59GB002.3", "SALIDA V454", "CCU", "PSG1_SO:04TI031AIN", "Alarma en K-5919", "ADIP3", "T452", 0, 600, "Notificar al Operador Externo para revisar en campo el tipo de falla", "Revisar en campo e Informar el tipo de falla")

        viewModelHistoricAlarm.addNewAlarm("PSG1_GE:04PA443CIN","130",2,"12/10/2022 6:10PM")
        viewModelHistoricAlarm.addNewAlarm("PSG1_SO:04TI031AIN","670",5,"12/10/2022 5:40PM")
        viewModelHistoricAlarm.addNewAlarm("PSG1_GE:04PA443CIN","rtn",4,"12/10/2022 3:13PM")
        viewModelHistoricAlarm.addNewAlarm("PSG1_SO:04TI031AIN","rtn",2,"11/10/2022 6:20PM")
        viewModelHistoricAlarm.addNewAlarm("PSG1_GE:04PA443CIN","rtn",3,"11/10/2022 10:05AM")

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment OperatorAlarm.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            OperatorAlarm().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}