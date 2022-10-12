package ar.edu.ort.jefud_notifying_system.view.panelist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import ar.edu.ort.jefud_notifying_system.R
import ar.edu.ort.jefud_notifying_system.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class PanelistActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupBottomNavBar()

    }

    private fun setupBottomNavBar() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_panelist) as NavHostFragment

        val navController = navHostFragment.navController

        findViewById<BottomNavigationView>(R.id.botNavPanelist).setupWithNavController(navController)
    }
    
}