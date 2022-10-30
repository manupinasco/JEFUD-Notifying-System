package ar.edu.ort.jefud_notifying_system.view.panelist

import android.app.*
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.provider.Contacts.GroupMembership.GROUP_ID
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import ar.edu.ort.jefud_notifying_system.R
import ar.edu.ort.jefud_notifying_system.database.JEFUDApplication
import ar.edu.ort.jefud_notifying_system.model.Message
import ar.edu.ort.jefud_notifying_system.view.MainActivity
import ar.edu.ort.jefud_notifying_system.viewmodel.MessageViewModel
import ar.edu.ort.jefud_notifying_system.viewmodel.MessageViewModelFactory
import ar.edu.ort.jefud_notifying_system.viewmodel.UsersViewModel
import ar.edu.ort.jefud_notifying_system.viewmodel.UsersViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*


class PanelistActivity : AppCompatActivity() {

    val positiveButtonClick = { dialog: DialogInterface, which: Int ->
        logout()
    }
    val negativeButtonClick = { dialog: DialogInterface, which: Int ->

    }

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




    }

    private fun getMessages(messages: List<Message>?) {
        val userDetails = this.getSharedPreferences("userdetails",
            Context.MODE_PRIVATE
        )
        val userDni = userDetails.getString("dni", "")

        var newMessages = false
        var i = 0
        if(messages != null && userDni != null)
            while(!newMessages && i < messages.size) {

                if(messages[i].dniRecipient.compareTo(userDni) == 0 && !messages[i].read) {
                    newMessages = true
                }
                i++
            }

        if(newMessages) {
            createNotificationBuilder()
            if (messages != null) {
                for(message in messages) {
                    message.read = true
                    viewModelMessages.updateMessage(message)
                }
            }
        }


    }

    private fun createNotificationBuilder() {



            val notificationManager : NotificationManager =  getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val notificationChannelGroup = NotificationChannelGroup(
                GROUP_ID,
                applicationContext.getString(R.string.GROUP_NAME)
            )
            notificationManager.createNotificationChannelGroup(notificationChannelGroup)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                var notificationChannel  = NotificationChannel("NOTIFICATION_URGENT _ID", "My Notifications", NotificationManager.IMPORTANCE_HIGH);
                notificationChannel.description = "Channel description"
                notificationChannel.enableLights(true)
                notificationChannel.lightColor = Color.RED
                notificationChannel.enableVibration(true)

                notificationChannel.group = GROUP_ID
                notificationChannel.setShowBadge(true)
                notificationManager.createNotificationChannel(notificationChannel)
            }


            var notificationBuilder : NotificationCompat.Builder = NotificationCompat.Builder(this, "NOTIFICATION_URGENT _ID");

            notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(ar.edu.ort.jefud_notifying_system.R.drawable.semicircle_login)
                .setTicker("Mensajes")
                .setContentTitle("RAIZEN")
                .setContentIntent(onClick())
                .setContentText("Nuevos mensajes sin leer")
                .setContentInfo("");


            var random = Random();
            var m = random.nextInt(9999 - 1000) + 1000;
            notificationManager.notify(/*notification id*/m, notificationBuilder.build());


        }

    private fun onClick(): PendingIntent? {


        return NavDeepLinkBuilder(this)
            .setComponentName(PanelistActivity::class.java)
            .setGraph(R.navigation.panelist_nav_graph)
            .setDestination(R.id.panelistMessagesReceived)
            .createPendingIntent()
    }




    fun basicAlert(view: View){

        val builder = AlertDialog.Builder(this)

        with(builder)
        {
            setTitle("RAIZEN NOTIFYING APP")
            setMessage("¿Confirmar cierre de sesión?")
            setNegativeButton("Sí", DialogInterface.OnClickListener(function = positiveButtonClick))
            setPositiveButton("Cancelar", negativeButtonClick)
            show()
        }


    }


    private fun logout() {

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