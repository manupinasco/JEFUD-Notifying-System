package ar.edu.ort.jefud_notifying_system.dao

import androidx.room.*
import ar.edu.ort.jefud_notifying_system.model.HistoricAlarm
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoricAlarmDao {
    @Query("SELECT * FROM historicalarm")
    fun getAll(): Flow<List<HistoricAlarm>>

    @Query("SELECT * from historicalarm WHERE tagName = :tagName LIMIT 1")
    fun getAlarmByTag(tagName: String): Flow<HistoricAlarm>


    @Delete
    fun delete(historicAlarm: HistoricAlarm)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(alarm: HistoricAlarm)
}