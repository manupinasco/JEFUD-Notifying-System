package ar.edu.ort.jefud_notifying_system.view.operator

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ar.edu.ort.jefud_notifying_system.databinding.ActivityMainBinding

class OperatorActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}