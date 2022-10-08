package ar.edu.ort.jefud_notifying_system.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ar.edu.ort.jefud_notifying_system.database.user.User

abstract class AppDatabase: RoomDatabase() {
    @Database(entities = arrayOf(User::class), version = 1)
    //abstract fun scheduleDao(): ScheduleDao
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "app_database")
                    .createFromAsset("database/notifying_system.db")
                    .build()
                INSTANCE = instance

                instance
            }
        }
    }
}
