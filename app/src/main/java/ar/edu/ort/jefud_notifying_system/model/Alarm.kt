package ar.edu.ort.jefud_notifying_system.model

import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

    @Entity
    data class Alarm (
        @PrimaryKey(autoGenerate = true)
        val id: Int = 0,
        @NonNull @ColumnInfo(name= "nameVariable") val nameVariable: String,
        @NonNull @ColumnInfo(name= "description") val description: String,
        @NonNull @ColumnInfo(name= "panel") val panel: String,
        @NonNull @ColumnInfo(name= "tagName") val tagName: String,
        @NonNull @ColumnInfo(name= "textName") val textName: String,
        @NonNull @ColumnInfo(name= "plant") val plant: String,
        @NonNull @ColumnInfo(name= "equipment") val equipment: String,
        @NonNull @ColumnInfo(name= "min") val min: Int,
        @NonNull @ColumnInfo(name= "max") val max: Int,
        @NonNull @ColumnInfo(name= "panelistAction") val panelistAction: String,
        @NonNull @ColumnInfo(name= "operatorAction") val operatorAction: String,
        @Nullable @ColumnInfo(name= "infoExtra") val infoExtra: String?,

        )

