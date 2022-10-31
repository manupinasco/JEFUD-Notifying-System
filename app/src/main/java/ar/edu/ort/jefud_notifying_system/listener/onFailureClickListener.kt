package ar.edu.ort.jefud_notifying_system.listener

import ar.edu.ort.jefud_notifying_system.model.Failure

interface onFailureClickListener {
    fun onFailureItemDetail(failure: Failure)
}