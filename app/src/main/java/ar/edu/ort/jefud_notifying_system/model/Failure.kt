package ar.edu.ort.jefud_notifying_system.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Failure (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @NonNull @ColumnInfo(name= "plant") val plant: String,
    @NonNull @ColumnInfo(name= "value") val value: String,
    @NonNull @ColumnInfo(name= "equipment") val equipment: String,
    @NonNull @ColumnInfo(name= "task") val task: String,
    @NonNull @ColumnInfo(name= "solved") var solved: Boolean,
    @NonNull @ColumnInfo(name= "panel") val panel: String,


    )