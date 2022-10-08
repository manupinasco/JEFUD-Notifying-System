package ar.edu.ort.jefud_notifying_system.database.user

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User (
    @PrimaryKey val id: Int,
    @NonNull @ColumnInfo(name = "dni") val dni: String
)