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

    @Query("SELECT * from historicalarm WHERE tagName = :tagName")
    fun getAlarmsByTag(tagName: String): Flow<List<HistoricAlarm>>

    @Query("SELECT * from historicalarm WHERE tagName = :tagName and CAST(SUBSTR(datetime, 4, 5) AS STRING) = :month")
    fun getAlarmsByTagAndMonth(tagName: String, month: String): Flow<List<HistoricAlarm>>

    @Query("SELECT * from historicalarm WHERE tagName = :tagName and CAST(SUBSTR(datetime, 4, 5) AS STRING) = :month and plant = :plant and priority < 5")
    fun getAlarmsByTagAndMonthAndPlant(tagName: String, month: String, plant: String): Flow<List<HistoricAlarm>>

    @Query("SELECT * from historicalarm WHERE tagName = :tagName and CAST(SUBSTR(datetime, 4, 5) AS STRING) = :month and panel = :panel and priority < 5")
    fun getAlarmsByTagAndMonthAndPanel(tagName: String, month: String, panel: String): Flow<List<HistoricAlarm>>

    @Query("SELECT * from historicalarm WHERE panel = :panel and priority < 5 and CAST(SUBSTR(datetime, 4, 5) AS STRING) = :month")
    fun getAlarmsByPanel(month: String, panel: String): Flow<List<HistoricAlarm>>

    @Query("SELECT * from historicalarm WHERE plant = :plant and priority < 5 and CAST(SUBSTR(datetime, 4, 5) AS STRING) = :month")
    fun getAlarmsByPlant(month: String, plant: String): Flow<List<HistoricAlarm>>

    @Delete
    suspend fun delete(historicAlarm: HistoricAlarm)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(alarm: HistoricAlarm)
}