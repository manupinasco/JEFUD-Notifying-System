package ar.edu.ort.jefud_notifying_system.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @NonNull @ColumnInfo(name= "dni") val dni: String,
    @NonNull @ColumnInfo(name= "password") val password: String,
    @NonNull @ColumnInfo(name= "rol") val rol: String,
    @NonNull @ColumnInfo(name= "panel") val panel: String,
        )