package ar.edu.ort.jefud_notifying_system.view

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.fragment.app.viewModels
import ar.edu.ort.jefud_notifying_system.R
import ar.edu.ort.jefud_notifying_system.database.JEFUDApplication
import ar.edu.ort.jefud_notifying_system.view.coordinator.CoordinatorActivity
import ar.edu.ort.jefud_notifying_system.view.manager.ManagerActivity
import ar.edu.ort.jefud_notifying_system.view.operator.OperatorActivity
import ar.edu.ort.jefud_notifying_system.view.panelist.PanelistActivity
import ar.edu.ort.jefud_notifying_system.viewmodel.MessageViewModel
import ar.edu.ort.jefud_notifying_system.viewmodel.MessageViewModelFactory
import ar.edu.ort.jefud_notifying_system.viewmodel.UserLoggedViewModel
import ar.edu.ort.jefud_notifying_system.viewmodel.UserLoggedViewModelFactory


class MainActivity : AppCompatActivity() {
    private val viewModelUserLogged: UserLoggedViewModel by viewModels {
        UserLoggedViewModelFactory(
            (this.application as JEFUDApplication).database
                .userLoggedDao()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModelUserLogged.retrieveUser().observe(this) {
                userLogged ->
            if(userLogged != null) {
                if(userLogged.stayLoggedIn) {
                    when (userLogged.role) {
                        "PANELIST" -> startActivity(
                            Intent(
                                this,
                                PanelistActivity::class.java
                            ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        )
                        "OPERATOR" -> startActivity(
                            Intent(
                                this,
                                OperatorActivity::class.java
                            ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        )
                        "MANAGER" -> startActivity(
                            Intent(
                                this,
                                ManagerActivity::class.java
                            ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        )
                        "COORDINATOR" -> startActivity(
                            Intent(
                                this,
                                CoordinatorActivity::class.java
                            ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        )
                    }
                }
                else {
                    viewModelUserLogged.delete()
                }
            }
            else {
                setContentView(R.layout.activity_main)
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            }

        }

    }



}