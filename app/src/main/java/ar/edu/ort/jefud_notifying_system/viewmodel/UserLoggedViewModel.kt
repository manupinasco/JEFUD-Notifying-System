package ar.edu.ort.jefud_notifying_system.viewmodel

import androidx.lifecycle.*
import ar.edu.ort.jefud_notifying_system.dao.UserDao
import ar.edu.ort.jefud_notifying_system.dao.UserLoggedDao
import ar.edu.ort.jefud_notifying_system.model.User
import ar.edu.ort.jefud_notifying_system.model.UserLogged
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class UserLoggedViewModel(private val userLoggedDao: UserLoggedDao) : ViewModel() {


    fun addUserLogged(userLogged: UserLogged) {
            val user = userLoggedDao.getUserLogged()
        runBlocking(Dispatchers.IO) {
            delete(user.first())
            insertUser(userLogged)
        }



    }

    private fun insertUser(user: UserLogged) {
        viewModelScope.launch {
            userLoggedDao.insert(user)
        }
    }


    fun delete(user: UserLogged) {
        deleteUserLogged(user)
    }

    private fun deleteUserLogged(user: UserLogged) {
        viewModelScope.launch {
            userLoggedDao.delete(user)
        }
    }

    fun retrieveUser(): LiveData<UserLogged> {
        return userLoggedDao.getUserLogged().asLiveData()

    }
}


class UserLoggedViewModelFactory(private val userLoggedDao: UserLoggedDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserLoggedViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserLoggedViewModel(userLoggedDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}