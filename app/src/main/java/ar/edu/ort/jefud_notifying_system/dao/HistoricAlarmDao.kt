package ar.edu.ort.jefud_notifying_system.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ar.edu.ort.jefud_notifying_system.model.HistoricAlarm
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoricAlarmDao {
    @Query("SELECT * FROM historicalarm")
    fun getAll(): Flow<List<HistoricAlarm>>

    @Query("SELECT * from historicalarm WHERE tagName = :tagName LIMIT 1")
    fun getAlarmByTag(tagName: String): Flow<HistoricAlarm>


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(alarm: HistoricAlarm)
}