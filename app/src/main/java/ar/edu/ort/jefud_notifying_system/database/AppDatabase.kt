package ar.edu.ort.jefud_notifying_system.database

import android.app.Application
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ar.edu.ort.jefud_notifying_system.dao.*
import ar.edu.ort.jefud_notifying_system.model.*
import java.io.File

@Database(entities = [User::class, Alarm::class, HistoricAlarm::class, Message::class, Failure::class], version = 18)
abstract class AppDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun alarmDao(): AlarmDao
    abstract fun historicAlarmDao(): HistoricAlarmDao
    abstract fun messageDao(): MessageDao
    abstract fun failureDao(): FailureDao

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

