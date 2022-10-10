package ar.edu.ort.jefud_notifying_system.viewmodel

import androidx.lifecycle.*
import ar.edu.ort.jefud_notifying_system.model.User
import ar.edu.ort.jefud_notifying_system.dao.UserDao
import kotlinx.coroutines.launch


class UsersViewModel(private val userDao: UserDao) : ViewModel() {
    val allUsers: LiveData<List<User>> = userDao.getAll().asLiveData()

        fun addNewUser(dni: String, password: String, rol: String) {
        val newUser = getNewUserEntry(dni, password, rol)
        insertUser(newUser)
    }

    private fun insertUser(user: User) {
        viewModelScope.launch {
            userDao.insert(user)
        }
    }

    private fun getNewUserEntry(dni: String, password: String, rol: String): User {
        return User(
            dni = dni,
            password = password,
            rol = rol
        )
    }

    fun retrieveUser(dni: String): LiveData<User> {
        return userDao.getUser(dni).asLiveData()
    }
}


class UsersViewModelFactory(private val userDao: UserDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UsersViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UsersViewModel(userDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}