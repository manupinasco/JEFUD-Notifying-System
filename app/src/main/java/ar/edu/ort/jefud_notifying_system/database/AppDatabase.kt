package ar.edu.ort.jefud_notifying_system.database

import android.app.Application
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ar.edu.ort.jefud_notifying_system.dao.AlarmDao
import ar.edu.ort.jefud_notifying_system.dao.HistoricAlarmDao
import ar.edu.ort.jefud_notifying_system.model.User
import ar.edu.ort.jefud_notifying_system.dao.UserDao
import ar.edu.ort.jefud_notifying_system.model.Alarm
import ar.edu.ort.jefud_notifying_system.model.HistoricAlarm
import java.io.File

@Database(entities = [User::class, Alarm::class, HistoricAlarm::class], version = 6)
abstract class AppDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun alarmDao(): AlarmDao
    abstract fun historicAlarmDao(): HistoricAlarmDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .createFromFile(File("C:/Users/Emanuel/Desktop/database/app.db"))
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                return instance
            }
        }
    }
}

class JEFUDApplication : Application() {
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this)}
    }

