package ar.edu.ort.jefud_notifying_system.model

import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserLogged(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @NonNull @ColumnInfo(name= "dni") val dni: String,
    @NonNull @ColumnInfo(name= "name") val name: String,
    @NonNull @ColumnInfo(name= "surname") val surname: String,
    @NonNull @ColumnInfo(name= "stayLoggedIn") val stayLoggedIn: Boolean,
    @NonNull @ColumnInfo(name= "role") val role: String,
    @Nullable @ColumnInfo(name= "panel") val panel: String?,
    @NonNull @ColumnInfo(name= "plant") val plant: String,
    @Nullable @ColumnInfo(name= "shift") val shift: String?,
)