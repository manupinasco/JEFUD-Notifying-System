package ar.edu.ort.jefud_notifying_system.dao

import androidx.room.*
import ar.edu.ort.jefud_notifying_system.model.Alarm
import kotlinx.coroutines.flow.Flow

@Dao
interface AlarmDao {
    @Query("SELECT * FROM alarm")
    fun getAll(): Flow<List<Alarm>>

    @Query("SELECT * from alarm WHERE tagName = :tagName LIMIT 1")
    fun getAlarmByTag(tagName: String): Flow<Alarm>

    @Query("SELECT * from alarm WHERE equipment = :equipment LIMIT 1")
    fun getAlarmByEquipment(equipment: String): Flow<Alarm>

    @Delete
    suspend fun delete(alarm: Alarm)

    @Query("SELECT * from alarm WHERE plant = :plant LIMIT 1")
    fun getAlarmByPlant(plant: String): Flow<Alarm>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(alarm: Alarm)
}