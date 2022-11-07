package ar.edu.ort.jefud_notifying_system.dao

import androidx.room.*
import ar.edu.ort.jefud_notifying_system.model.Failure
import ar.edu.ort.jefud_notifying_system.model.Message
import kotlinx.coroutines.flow.Flow

@Dao
interface FailureDao {
    @Query("SELECT * FROM failure")
    fun getAll(): Flow<List<Failure>>

    @Query("SELECT * from failure WHERE plant = :plant and panel = :panel and solved = 0")
    fun getFailuresByPlantAndPanel(plant: String, panel: String): Flow<List<Failure>>

    @Query("SELECT * from failure WHERE panel = :panel and active == 1")
    fun getFailuresByPanel(panel: String): Flow<List<Failure>>

    @Query("SELECT * from failure WHERE plant = :plant and active == 1")
    fun getFailuresByPlant(plant: String): Flow<List<Failure>>

    @Query("SELECT * from failure WHERE equipment = :equipment and solved == 0")
    fun getFailureUnsolvedByEquipment(equipment: String): Flow<Failure>

    @Query("SELECT * from failure WHERE equipment = :equipment and active == 1")
    fun getFailuresEquipment(equipment: String): Flow<List<Failure>>

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun updateFailure(failure: Failure)

    @Delete
    suspend fun delete(failure: Failure)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(failure: Failure)
}