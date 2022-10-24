package ar.edu.ort.jefud_notifying_system.view.panelist

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ar.edu.ort.jefud_notifying_system.R
import ar.edu.ort.jefud_notifying_system.adapter.AlarmsPanelistAdapter
import ar.edu.ort.jefud_notifying_system.adapter.AlarmsRecordAdapter
import ar.edu.ort.jefud_notifying_system.database.JEFUDApplication
import ar.edu.ort.jefud_notifying_system.databinding.FragmentAlarmBinding
import ar.edu.ort.jefud_notifying_system.model.HistoricAlarm
import ar.edu.ort.jefud_notifying_system.viewmodel.AlarmsViewModel
import ar.edu.ort.jefud_notifying_system.viewmodel.AlarmsViewModelFactory
import ar.edu.ort.jefud_notifying_system.viewmodel.HistoricAlarmsViewModel
import ar.edu.ort.jefud_notifying_system.viewmodel.HistoricAlarmsViewModelFactory


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PanelistAlarm.newInstance] factory method to
 * create an instance of this fragment.
 */
class PanelistAlarm : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var recAlarm : RecyclerView
    private lateinit var recAlarmRecord : RecyclerView

    private var alarmsList : MutableList<HistoricAlarm> = ArrayList<HistoricAlarm>()
    private var alarmsRecord : MutableList<HistoricAlarm> = ArrayList<HistoricAlarm>()

    private lateinit var linearLayoutManager: LinearLayoutManager

    private lateinit var alarmListAdapter: AlarmsPanelistAdapter
    private lateinit var alarmRecordListAdapter: AlarmsRecordAdapter
    private var _binding: FragmentAlarmBinding? = null
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
    private lateinit var vista : View

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
        _binding = FragmentAlarmBinding.inflate(inflater, container, false)
        vista = inflater.inflate(R.layout.fragment_alarm, container, false)

        addData()

        viewModelHistoricAlarm.allAlarms.observe(this.viewLifecycleOwner) { alarms ->
            getAlarms(alarms)

        }

        recAlarmRecord = binding.alarmsRecord
        recAlarmRecord.removeAllViews()
        recAlarmRecord.setHasFixedSize(true)
        linearLayoutManager = LinearLayoutManager(context)

        recAlarmRecord.layoutManager = linearLayoutManager


        alarmRecordListAdapter = AlarmsRecordAdapter(alarmsRecord)



        recAlarmRecord.adapter = alarmRecordListAdapter



        recAlarm = binding.alarmsRecyclerView
        alarmsList.clear()
        recAlarm.setHasFixedSize(true)
        linearLayoutManager = LinearLayoutManager(context)

        recAlarm.layoutManager = linearLayoutManager


        alarmListAdapter = AlarmsPanelistAdapter(alarmsList, (activity?.application as JEFUDApplication).database
            .alarmDao())



        recAlarm.adapter = alarmListAdapter
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        // Se borran los datos de las listas para que no se dupliquen al volver a esta vista
        alarmsList.clear()
        alarmsRecord.clear()
    }


    private fun addData() {

        viewModelAlarm.allAlarms.observe(this.viewLifecycleOwner) { alarms ->
            if(alarms.size == 0) {
                viewModelAlarm.addNewAlarm("04PA443.1", "ROTURA SELLO P-456", "CCU", "PSG1_GE:04PA443CIN", "ROTURA DE SELLO P-456", "ADIP1", "P-456", 0, 100, "Pedir al operador externo que verifique el estado de la bomba.", "Verificar el estado de la bomba. Si se rompió el sello; detener la bomba/bloquearla y marchar la auxiliar; de lo contrario; la alarma se dispara por rotura del presostato (pedir reparación del mismo y controlar rutinariamente la indicación del manómetro local).", "IOBAD")
                viewModelAlarm.addNewAlarm("59GB002.3", "SALIDA V454", "CCU", "PSG1_SO:04TI031AIN", "Alarma en K-5919", "ADIP3", "T452", 0, 600, "Notificar al Operador Externo para revisar en campo el tipo de falla", "Revisar en campo e Informar el tipo de falla", null)

            }
        }


        viewModelHistoricAlarm.allAlarms.observe(this.viewLifecycleOwner) { alarms ->
            if(alarms.size == 0) {
                viewModelHistoricAlarm.addNewAlarm("PSG1_GE:04PA443CIN","130",2,"12/10/2022 6:10PM")
                viewModelHistoricAlarm.addNewAlarm("PSG1_SO:04TI031AIN","670",5,"12/10/2022 5:40PM")
                viewModelHistoricAlarm.addNewAlarm("PSG1_GE:04PA443CIN","rtn",4,"12/10/2022 3:13PM")
                viewModelHistoricAlarm.addNewAlarm("PSG1_SO:04TI031AIN","rtn",2,"11/10/2022 6:20PM")
                viewModelHistoricAlarm.addNewAlarm("PSG1_GE:04PA443CIN","rtn",3,"11/10/2022 10:05AM")
            }
        }




    }

    private fun getAlarms(alarms: List<HistoricAlarm>?) {
        val userDetails = requireContext().getSharedPreferences("userdetails", MODE_PRIVATE)
        val userPanel = userDetails.getString("panel", "")

        if(alarms != null) {
            for (i in alarms.indices) {

                viewModelAlarm.retrieveAlarmByTag(alarms[i].tagName).observe(this.viewLifecycleOwner) { alarm ->

                    if(userPanel != null) {
                        if(alarm.panel.compareTo(userPanel) == 0) {
                            if(alarms[i].value.compareTo("rtn") == 0) {
                                if(alarmsRecord.size < 3) {
                                    alarmsRecord.add(alarms[i])
                                }
                            }
                            else {
                                alarmsList.add(alarms[i])
                            }
                        }

                        activity?.runOnUiThread(Runnable {
                            alarmRecordListAdapter.notifyDataSetChanged()
                        })
                        activity?.runOnUiThread(Runnable {
                            alarmListAdapter.notifyDataSetChanged()
                        })
                    }

                }


            }

        }
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PanelistAlarm.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PanelistAlarm().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

