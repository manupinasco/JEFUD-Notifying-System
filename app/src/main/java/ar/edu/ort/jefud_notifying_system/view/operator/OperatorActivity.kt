package ar.edu.ort.jefud_notifying_system.view.operator

import android.app.*
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.provider.Contacts
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavDeepLinkBuilder
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import ar.edu.ort.jefud_notifying_system.R
import ar.edu.ort.jefud_notifying_system.database.JEFUDApplication
import ar.edu.ort.jefud_notifying_system.databinding.ActivityMainBinding
import ar.edu.ort.jefud_notifying_system.model.Alarm
import ar.edu.ort.jefud_notifying_system.model.HistoricAlarm
import ar.edu.ort.jefud_notifying_system.model.Message
import ar.edu.ort.jefud_notifying_system.view.MainActivity
import ar.edu.ort.jefud_notifying_system.view.panelist.PanelistActivity
import ar.edu.ort.jefud_notifying_system.viewmodel.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*

class OperatorActivity : AppCompatActivity() {

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

    private val viewModelHistoricAlarm: HistoricAlarmsViewModel by viewModels {
        HistoricAlarmsViewModelFactory(
            (this.application as JEFUDApplication).database
                .historicAlarmDao()
        )
    }

    private lateinit var notificationManager : NotificationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_operator)
        setupBottomNavBar()
        createNotificationChannel()

        viewModelMessages.allMessages.observe(this) {messages ->

            createNotificationMessages(messages)


        }

        viewModelHistoricAlarm.allAlarms.observe(this) {
            alarms ->
            createNotificationAlarms(alarms)
        }




    }

    private fun createNotificationAlarms(alarms: List<HistoricAlarm>?) {
        val userDetails = this.getSharedPreferences("userdetails",
            Context.MODE_PRIVATE
        )
        val panel = userDetails.getString("panel", "")

        var newAlarms = false
        var i = 0
        if(alarms != null && panel != null)
            while(!newAlarms && i < alarms.size) {

                if(alarms[i].panel.compareTo(panel) == 0 && alarms[i].value.compareTo("rtn") != 0) {
                    newAlarms = true
                }
                i++
            }

        if(newAlarms) {
            createNotificationBuilderAlarms()
        }
    }

    private fun createNotificationMessages(messages: List<Message>?) {
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
            Contacts.GroupMembership.GROUP_ID,
            applicationContext.getString(R.string.GROUP_NAME)
        )
        notificationManager.createNotificationChannelGroup(notificationChannelGroup)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            var notificationChannel  = NotificationChannel("NOTIFICATION_URGENT _ID", "My Notifications", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.description = "Channel description"
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)

            notificationChannel.group = Contacts.GroupMembership.GROUP_ID
            notificationChannel.setShowBadge(true)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    private fun createNotificationBuilderAlarms() {






        var notificationBuilder : NotificationCompat.Builder = NotificationCompat.Builder(this, "NOTIFICATION_URGENT _ID");

        notificationBuilder.setAutoCancel(true)
            .setDefaults(Notification.DEFAULT_ALL)
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(R.drawable.ic_alert_alarm)
            .setTicker("Alarmas")
            .setContentTitle("RAIZEN")
            .setContentIntent(onClickAlarm())
            .setContentText("Nuevas alarmas sin solucionar")
            .setContentInfo("");


        var random = Random();
        var m = random.nextInt(9999 - 1000) + 1000;
        notificationManager.notify(/*notification id*/m, notificationBuilder.build());


    }

    private fun onClickAlarm(): PendingIntent? {


        return NavDeepLinkBuilder(this)
            .setComponentName(OperatorActivity::class.java)
            .setGraph(R.navigation.operator_nav_graph)
            .setDestination(R.id.operatorAlarm)
            .createPendingIntent()
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
            .setComponentName(OperatorActivity::class.java)
            .setGraph(R.navigation.operator_nav_graph)
            .setDestination(R.id.operatorMessagesReceived)
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
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_operator) as NavHostFragment

        val navController = navHostFragment.navController

        findViewById<BottomNavigationView>(R.id.botNavOperator).setupWithNavController(navController)
    }
}