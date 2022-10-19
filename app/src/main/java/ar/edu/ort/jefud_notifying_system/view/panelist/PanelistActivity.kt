package ar.edu.ort.jefud_notifying_system.view.panelist

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import ar.edu.ort.jefud_notifying_system.R
import ar.edu.ort.jefud_notifying_system.database.JEFUDApplication
import ar.edu.ort.jefud_notifying_system.model.Message
import ar.edu.ort.jefud_notifying_system.model.User
import ar.edu.ort.jefud_notifying_system.view.MainActivity
import ar.edu.ort.jefud_notifying_system.viewmodel.MessageViewModel
import ar.edu.ort.jefud_notifying_system.viewmodel.MessageViewModelFactory
import ar.edu.ort.jefud_notifying_system.viewmodel.UsersViewModel
import ar.edu.ort.jefud_notifying_system.viewmodel.UsersViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView

class PanelistActivity : AppCompatActivity() {

    private var usersList : MutableList<User> = ArrayList<User>()

    private val viewModelMessages: MessageViewModel by viewModels {
        MessageViewModelFactory(
            (this.application as JEFUDApplication).database
                .messageDao()
        )
    }

    private val viewModelUsers: UsersViewModel by viewModels {
        UsersViewModelFactory(
            (this.application as JEFUDApplication).database
                .userDao()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_panelista)
        setupBottomNavBar()
        viewModelMessages.allMessages.observe(this) {messages ->

            getMessages(messages)
        }

        for(i in 0..(usersList.size-1)) {
            val intent = Intent(this, PanelistActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)


            val builder = NotificationCompat.Builder(this, "0")
                .setSmallIcon(R.drawable.alarms_icon_1)
                .setContentTitle("New Messages")
                .setContentText("From " + usersList[i].name)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
        }


    }

    private fun getMessages(messages: List<Message>?) {
        val userDetails = this.getSharedPreferences("userdetails",
            Context.MODE_PRIVATE
        )
        val userDni = userDetails.getString("dni", "")

        if(messages != null && userDni != null)
            for (i in 0..(messages.size-1)) {
                if(messages[i].dniRecipient.compareTo(userDni) == 0 && !messages[i].read) {
                    viewModelUsers.retrieveUser(messages[i].dniSender).observe(this) { user ->
                        usersList.add(user)
                    }
                }
            }

    }

    override fun onStart() {
        super.onStart()

        var btn1 = findViewById<Button>(R.id.button)
        btn1.setOnClickListener{
            logout()
        }
    }

    private fun logout() {
        //preguntar primero
        val userDetails = this.getSharedPreferences("userdetails",
            Context.MODE_PRIVATE
        )
        val edit: SharedPreferences.Editor = userDetails.edit()
        edit.putString("dni", "")
        edit.putString("password", "")
        edit.putString("panel", "")
        edit.putString("role", "")
        edit.apply()
        startActivity(Intent(this, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
    }

    private fun setupBottomNavBar() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_panelist) as NavHostFragment

        val navController = navHostFragment.navController

        findViewById<BottomNavigationView>(R.id.botNavPanelist).setupWithNavController(navController)
    }
    
}