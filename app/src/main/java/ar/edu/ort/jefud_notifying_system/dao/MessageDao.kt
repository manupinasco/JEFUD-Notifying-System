package ar.edu.ort.jefud_notifying_system.dao

import androidx.room.*
import ar.edu.ort.jefud_notifying_system.model.Message
import ar.edu.ort.jefud_notifying_system.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {
    @Query("SELECT * FROM message")
    fun getAll(): Flow<List<Message>>

    @Query("SELECT * from message WHERE dniRecipient = :dni")
    fun getMessagesByRecipient(dni: String): Flow<List<Message>>

    @Query("SELECT * from message WHERE dniSender = :dni")
    fun getMessagesBySender(dni: String): Flow<List<Message>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(message: Message)

    @Update
    fun updateMessage(message: Message)
}