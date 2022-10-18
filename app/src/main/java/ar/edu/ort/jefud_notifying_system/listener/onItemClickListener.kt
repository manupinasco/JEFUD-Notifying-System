package ar.edu.ort.jefud_notifying_system.listener

import ar.edu.ort.jefud_notifying_system.model.Message
import ar.edu.ort.jefud_notifying_system.model.User

interface onItemClickListener {

    /**      * Se invoca cuando se selecciona un producto de la lista      */
fun onViewItemDetail(message: Message, user: User) }