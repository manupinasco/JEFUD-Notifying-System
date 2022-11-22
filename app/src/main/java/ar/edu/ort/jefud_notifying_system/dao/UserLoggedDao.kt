package ar.edu.ort.jefud_notifying_system.dao

import androidx.room.*
import ar.edu.ort.jefud_notifying_system.model.User
import ar.edu.ort.jefud_notifying_system.model.UserLogged
import kotlinx.coroutines.flow.Flow

@Dao
interface UserLoggedDao {


    @Query("SELECT * from userLogged LIMIT 1")
    fun getUserLogged(): Flow<UserLogged>
    @Query("DELETE from userLogged")
    suspend fun delete()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: UserLogged)
}
