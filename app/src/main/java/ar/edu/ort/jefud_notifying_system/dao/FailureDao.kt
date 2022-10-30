package ar.edu.ort.jefud_notifying_system.dao

import androidx.room.*
import ar.edu.ort.jefud_notifying_system.model.Failure
import kotlinx.coroutines.flow.Flow

@Dao
interface FailureDao {
    @Query("SELECT * FROM failure")
    fun getAll(): Flow<List<Failure>>

    @Query("SELECT * from failure WHERE plant = :plant and panel = :panel")
    fun getFailuresByPlantAndPanel(plant: String, panel: String): Flow<List<Failure>>


    @Delete
    suspend fun delete(failure: Failure)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(failure: Failure)
}