package ar.edu.ort.jefud_notifying_system.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class HistoricAlarm (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @NonNull @ColumnInfo(name= "tagName") val tagName: String,
    @NonNull @ColumnInfo(name= "value") val value: String,
    @NonNull @ColumnInfo(name= "priority") val priority: Int,
    @NonNull @ColumnInfo(name= "datetime") val datetime: String,

    )