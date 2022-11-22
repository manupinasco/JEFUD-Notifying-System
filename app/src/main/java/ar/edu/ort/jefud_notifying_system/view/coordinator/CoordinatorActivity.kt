package ar.edu.ort.jefud_notifying_system.view.coordinator

import android.app.*
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Contacts.GroupMembership.GROUP_ID
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavDeepLinkBuilder
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import ar.edu.ort.jefud_notifying_system.R
import ar.edu.ort.jefud_notifying_system.database.JEFUDApplication
import ar.edu.ort.jefud_notifying_system.model.Failure
import ar.edu.ort.jefud_notifying_system.model.Message
import ar.edu.ort.jefud_notifying_system.view.MainActivity
import ar.edu.ort.jefud_notifying_system.viewmodel.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*

class CoordinatorActivity : AppCompatActivity() {
    private val positiveButtonClick = { dialog: DialogInterface, which: Int ->
        logout()
    }
    private val negativeButtonClick = { dialog: DialogInterface, which: Int ->

    }
    private val viewModelMessages: MessageViewModel by viewModels {
        MessageViewModelFactory(
            (this.application as JEFUDApplication).database
                .messageDao()
        )
    }

    private val viewModelUserLogged: UserLoggedViewModel by viewModels {
        UserLoggedViewModelFactory(
            (this.application as JEFUDApplication).database
                .userLoggedDao()
        )
    }

    private lateinit var notificationManager : NotificationManager

    private val viewModelFailure: FailuresViewModel by viewModels {
        FailuresViewModelFactory(
            (this.application as JEFUDApplication).database
                .failureDao()
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coordinator)
        setupBottomNavBar()
        createNotificationChannel()
        viewModelMessages.allMessages.observe(this) {messages ->

            showNotificationMessages(messages)


        }
        val userDetails = this.getSharedPreferences("userdetails",
            Context.MODE_PRIVATE
        )
        val plant = userDetails.getString("plant", "")
        val panel = userDetails.getString("panel", "")
        if(plant != null && panel != null)
        viewModelFailure.retrieveFailureByPlantAndPanel(plant, panel).observe(this) {
            failures ->
            showNotificationFailures(failures)
        }


    }

    private fun showNotificationFailures(failures: List<Failure>?) {

        if(failures != null)
        if(failures.isNotEmpty()) {
            createNotificationBuilderFailures()
        }



    }

    private fun showNotificationMessages(messages: List<Message>?) {
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
            createNotificationBuilderMessages()
        }


    }

    private fun createNotificationChannel() {
        notificationManager =  getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

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

    }

    private fun createNotificationBuilderFailures() {





        var notificationBuilder : NotificationCompat.Builder = NotificationCompat.Builder(this, "NOTIFICATION_URGENT _ID");

        notificationBuilder.setAutoCancel(true)
            .setDefaults(Notification.DEFAULT_ALL)
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(R.drawable.ic_alert_alarm)
            .setTicker("Desvíos")
            .setContentTitle("RAIZEN")
            .setContentIntent(onClickFailure())
            .setContentText("Nuevos desvíos sin resolver")
            .setContentInfo("");


        var random = Random();
        var m = random.nextInt(9999 - 1000) + 1000;
        notificationManager.notify(/*notification id*/m, notificationBuilder.build());


    }

    private fun createNotificationBuilderMessages() {





        var notificationBuilder : NotificationCompat.Builder = NotificationCompat.Builder(this, "NOTIFICATION_URGENT _ID");

        notificationBuilder.setAutoCancel(true)
            .setDefaults(Notification.DEFAULT_ALL)
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(R.drawable.ic_outline_chat_bubble_outline)
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
            .setComponentName(CoordinatorActivity::class.java)
            .setGraph(R.navigation.coordinator_nav_graph)
            .setDestination(R.id.coordinatorMessagesReceived)
            .createPendingIntent()
    }

    private fun onClickFailure(): PendingIntent? {


        return NavDeepLinkBuilder(this)
            .setComponentName(CoordinatorActivity::class.java)
            .setGraph(R.navigation.coordinator_nav_graph)
            .setDestination(R.id.coordinatorFailures)
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
        edit.putString("panel", "")
        edit.putString("role", "")
        edit.apply()
        viewModelUserLogged.delete()
        startActivity(Intent(this, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
    }

    private fun setupBottomNavBar() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_coordinator) as NavHostFragment

        val navController = navHostFragment.navController

        findViewById<BottomNavigationView>(R.id.botNavCoordinator).setupWithNavController(navController)
    }
}