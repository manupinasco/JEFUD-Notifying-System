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

    @Query("SELECT * from user WHERE role = :role and plant = :plant LIMIT 1")
    fun getUser(role: String, plant: String): Flow<User>

    @Query("SELECT * from user WHERE role = :role and plant = :panel LIMIT 1")
    fun getUserByPanel(role: String, panel: String): Flow<User>

    @Query("SELECT * from user WHERE role = :role and plant = :panel and shift = :shift LIMIT 1")
    fun getUserByPanelAndShift(role: String, panel: String, shift: String): Flow<User>

    @Delete
    suspend fun delete(user: User)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: User)
}
