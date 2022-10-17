package ar.edu.ort.jefud_notifying_system.viewmodel

import androidx.lifecycle.*
import ar.edu.ort.jefud_notifying_system.dao.MessageDao
import ar.edu.ort.jefud_notifying_system.model.Message
import kotlinx.coroutines.launch

class MessageViewModel(private val messageDao: MessageDao): ViewModel() {
    val allMessages: LiveData<List<Message>> = messageDao.getAll().asLiveData()

    fun addNewMessage(dniRecipient: String, dniSender: String, message: String, read: Boolean) {
        val newMessage = getNewMessageEntry(dniRecipient, dniSender, message, read)
        insertMessage(newMessage)
    }

    private fun insertMessage(message: Message) {
        viewModelScope.launch {
            messageDao.insert(message)
        }
    }

    private fun getNewMessageEntry(dniRecipient: String, dniSender: String, message: String, read: Boolean): Message {
        return Message(
            dniRecipient = dniRecipient,
            dniSender = dniSender,
            message = message,
            read = read
        )
    }

    fun retrieveMessagesByDniRecipient(dni: String): LiveData<List<Message>> {
        return messageDao.getMessagesByRecipient(dni).asLiveData()
    }

    fun retrieveMessagesByDniSender(dni: String): LiveData<List<Message>> {
        return messageDao.getMessagesBySender(dni).asLiveData()
    }
}

class MessageViewModelFactory(private val messageDao: MessageDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MessageViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MessageViewModel(messageDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}