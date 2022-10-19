package ar.edu.ort.jefud_notifying_system.dao

import androidx.room.*
import ar.edu.ort.jefud_notifying_system.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAll(): Flow<List<User>>

    @Query("SELECT * from user WHERE dni = :dni LIMIT 1")
    fun getUser(dni: String): Flow<User>

    @Delete
    fun delete(user: User)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: User)
}
