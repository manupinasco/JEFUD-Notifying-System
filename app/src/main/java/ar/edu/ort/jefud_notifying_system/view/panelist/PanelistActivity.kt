package ar.edu.ort.jefud_notifying_system.view.panelist

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import ar.edu.ort.jefud_notifying_system.R
import ar.edu.ort.jefud_notifying_system.databinding.ActivityMainBinding
import ar.edu.ort.jefud_notifying_system.view.MainActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class PanelistActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_panelista)


       setupBottomNavBar()

    }

    override fun onStart() {
        super.onStart()

        var btn1 = findViewById<Button>(R.id.button)
        btn1.setOnClickListener{
            logout()
        }
    }

    private fun logout() {
        val sharedPrefDni = this?.getPreferences(Context.MODE_PRIVATE) ?: return
        with (sharedPrefDni.edit()) {
            putString(getString(ar.edu.ort.jefud_notifying_system.R.string.saved_userdni_key),"dni")
            apply()

        }

        val sharedPrefPanel = this?.getPreferences(Context.MODE_PRIVATE) ?: return
        with (sharedPrefPanel.edit()) {
            putString(getString(ar.edu.ort.jefud_notifying_system.R.string.saved_userpanel_key), "panel")
            apply()

        }

        val sharedPrefPassword = this?.getPreferences(Context.MODE_PRIVATE) ?: return
        with (sharedPrefPassword.edit()) {
            putString(getString(ar.edu.ort.jefud_notifying_system.R.string.saved_userpassword_key), "password")
            apply()
        }

        val sharedPrefRole = this?.getPreferences(Context.MODE_PRIVATE) ?: return
        with (sharedPrefRole.edit()) {
            putString(getString(ar.edu.ort.jefud_notifying_system.R.string.saved_userrole_key), "role")
            apply()
        }
        startActivity(Intent(this, MainActivity::class.java))
    }

    private fun setupBottomNavBar() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_panelist) as NavHostFragment

        val navController = navHostFragment.navController

        findViewById<BottomNavigationView>(R.id.botNavPanelist).setupWithNavController(navController)
    }
    
}