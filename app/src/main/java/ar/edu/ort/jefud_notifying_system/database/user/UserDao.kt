package ar.edu.ort.jefud_notifying_system.database.user

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM user ORDER BY dni ASC")
    fun getAll(): Flow<List<User>>

}