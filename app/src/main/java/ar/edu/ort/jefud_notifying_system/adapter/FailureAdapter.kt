package ar.edu.ort.jefud_notifying_system.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ar.edu.ort.jefud_notifying_system.R
import ar.edu.ort.jefud_notifying_system.dao.FailureDao
import ar.edu.ort.jefud_notifying_system.listener.onFailureClickListener
import ar.edu.ort.jefud_notifying_system.model.Failure
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class FailureAdapter(
    private val failuresList: MutableList<Failure>,
    private val onItemClick: onFailureClickListener,
    private val failureDao: FailureDao
): RecyclerView.Adapter<FailureViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FailureViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.failure_item, parent, false)
        return FailureViewHolder(view)
    }

    override fun onBindViewHolder(holder: FailureViewHolder, position: Int) {
        val failure: Failure
        runBlocking(Dispatchers.IO) {
            failure = failuresList[position]
        }
        holder.bind(failure)

        holder.getCardLayout().setOnClickListener{
            onItemClick.onFailureItemDetail(failuresList[position])
        }
    }

    override fun getItemCount(): Int {
        return failuresList.size
    }
}