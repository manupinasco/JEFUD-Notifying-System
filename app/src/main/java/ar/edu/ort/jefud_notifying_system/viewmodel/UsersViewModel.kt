package ar.edu.ort.jefud_notifying_system.viewmodel

import androidx.lifecycle.*
import ar.edu.ort.jefud_notifying_system.model.User
import ar.edu.ort.jefud_notifying_system.dao.UserDao
import kotlinx.coroutines.launch


class UsersViewModel(private val userDao: UserDao) : ViewModel() {
    val allUsers: LiveData<List<User>> = userDao.getAll().asLiveData()


        fun addNewUser(dni: String, password: String, role: String, panel: String?, name: String, surname: String, plant: String, shift: String?) {
        val newUser = getNewUserEntry(dni, password, role, panel, name, surname, plant, shift)
        insertUser(newUser)
    }

    private fun insertUser(user: User) {
        viewModelScope.launch {
            userDao.insert(user)
        }
    }

    private fun getNewUserEntry(dni: String, password: String, role: String, panel: String?, name: String, surname: String, plant: String, shift: String?): User {
        return User(
            dni = dni,
            password = password,
            role = role,
            panel = panel,
            name = name,
            surname = surname,
            plant = plant,
            shift = shift
        )
    }



    fun delete(user: User) {
        deleteUser(user)
    }

    private fun deleteUser(user: User) {
        viewModelScope.launch {
            userDao.delete(user)
        }
    }

    fun retrieveUser(dni: String): LiveData<User> {
        return userDao.getUser(dni).asLiveData()
    }

    fun retrieveUser(plant: String, role: String): LiveData<User> {
        return userDao.getUser(role, plant).asLiveData()
    }

    fun retrieveUserByPanel(panel: String, role: String): LiveData<User> {
        return userDao.getUserByPanel(role, panel).asLiveData()
    }

    fun retrieveUserByPanelAndShift(role: String, panel: String, shift: String): LiveData<User> {
        return userDao.getUserByPanelAndShift(role, panel, shift).asLiveData()
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