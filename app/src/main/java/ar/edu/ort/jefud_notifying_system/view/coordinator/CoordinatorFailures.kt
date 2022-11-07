package ar.edu.ort.jefud_notifying_system.view.coordinator

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.asFlow
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ar.edu.ort.jefud_notifying_system.adapter.FailureAdapter
import ar.edu.ort.jefud_notifying_system.database.JEFUDApplication
import ar.edu.ort.jefud_notifying_system.databinding.FragmentFailuresBinding
import ar.edu.ort.jefud_notifying_system.listener.onFailureClickListener
import ar.edu.ort.jefud_notifying_system.model.Failure
import ar.edu.ort.jefud_notifying_system.viewmodel.FailuresViewModel
import ar.edu.ort.jefud_notifying_system.viewmodel.FailuresViewModelFactory


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CoordinatorFailures.newInstance] factory method to
 * create an instance of this fragment.
 */
class CoordinatorFailures : Fragment(), onFailureClickListener {
    private lateinit var btnGoToDetails: TextView
    private var _binding: FragmentFailuresBinding? = null
    private val binding get() = _binding!!
    private lateinit var recFailure: RecyclerView
    private var failuresList: MutableList<Failure> = ArrayList<Failure>()
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var failureListAdapter: FailureAdapter
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
        _binding = FragmentFailuresBinding.inflate(inflater, container, false)

        viewModelFailure.allFailures.observe(this.viewLifecycleOwner) { failures ->
            getFailures(failures)
        }

        recFailure = binding.failureRecyclerView
        recFailure.setHasFixedSize(true)
        linearLayoutManager = LinearLayoutManager(context)

        recFailure.layoutManager = linearLayoutManager

        failureListAdapter = FailureAdapter(
            failuresList, this, (activity?.application as JEFUDApplication).database
                .failureDao()
        )

        recFailure.adapter = failureListAdapter


        return binding.root
    }

    override fun onStart() {
        super.onStart()
        failuresList.clear()

    }

    private fun addData(){
        viewModelFailure.allFailures.observe(this.viewLifecycleOwner) { failures ->
            if (failures.isEmpty()){
                viewModelFailure.addNewFailure("CCU", "Medir temperatura tanque 3", "ADIP1", "Tanque 3","500°")
                viewModelFailure.addNewFailure("CCU", "Medir temperatura tanque 1", "ADIP1", "Tanque 1","400°")
                viewModelFailure.addNewFailure("CCA", "Medir temperatura tanque 3", "ADIP1", "Tanque 3","500°")
                viewModelFailure.addNewFailure("CCU", "Medir temperatura tanque 3", "ADIP2", "Tanque 3","500°")
            }

        }
    }

    private fun getFailures(failures: List<Failure>) {
        val userDetails = requireContext().getSharedPreferences("userdetails",
            Context.MODE_PRIVATE
        )
        val userPanel = userDetails.getString("panel", "").toString()
        val userPlant = userDetails.getString("plant", "").toString()

        viewModelFailure.retrieveFailureByPlantAndPanel(userPlant, userPanel).observe(this.viewLifecycleOwner) {
            failures ->
            for (i in failures.indices) {
                failuresList.add(failures[i])
            }
            activity?.runOnUiThread(Runnable { failureListAdapter.notifyDataSetChanged() })
        }


    }


    override fun onFailureItemDetail(failure: Failure) {
        findNavController().navigate((CoordinatorFailuresDirections.actionCoordinatorFailuresToCoordinatorFailureDetails(failure.task, failure.value, failure.equipment)))
    }
}
