package ar.edu.ort.jefud_notifying_system.view.panelist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ar.edu.ort.jefud_notifying_system.R
import ar.edu.ort.jefud_notifying_system.databinding.ActivityMainBinding

class PanelistActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}