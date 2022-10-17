package ar.edu.ort.jefud_notifying_system.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Message (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @NonNull @ColumnInfo(name= "dniSender") val dniSender: String,
    @NonNull @ColumnInfo(name= "dniRecipient") val dniRecipient: String,
    @NonNull @ColumnInfo(name= "message") val message: String,
    @NonNull @ColumnInfo(name= "read") val read: Boolean
        )