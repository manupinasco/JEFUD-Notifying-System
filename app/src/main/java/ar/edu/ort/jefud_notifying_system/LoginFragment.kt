package ar.edu.ort.jefud_notifying_system

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import ar.edu.ort.jefud_notifying_system.panelista.PanelistaActivity


class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(v: View, savedInstanceState: Bundle?) {
        super.onViewCreated(v, savedInstanceState)
        val buttonLogin = v.findViewById<Button>(R.id.buttonLogin)

        buttonLogin.setOnClickListener{
            //if(user.rol == "panelista"){
            startActivity(Intent(requireContext(), PanelistaActivity::class.java))
            //}
        }

    }
}