package ar.edu.ort.jefud_notifying_system.view.operator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ar.edu.ort.jefud_notifying_system.R
import ar.edu.ort.jefud_notifying_system.databinding.FragmentLoginBinding

class AlarmFragment: Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var vista : View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root}

    override fun onStart() {
        super.onStart()


    }


}