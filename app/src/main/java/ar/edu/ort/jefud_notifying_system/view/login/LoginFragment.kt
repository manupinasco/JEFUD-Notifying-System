package ar.edu.ort.jefud_notifying_system.view.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import ar.edu.ort.jefud_notifying_system.R
import ar.edu.ort.jefud_notifying_system.database.JEFUDApplication
import ar.edu.ort.jefud_notifying_system.databinding.FragmentLoginBinding
import ar.edu.ort.jefud_notifying_system.model.User
import ar.edu.ort.jefud_notifying_system.view.panelist.PanelistActivity
import ar.edu.ort.jefud_notifying_system.viewmodel.UsersViewModel
import ar.edu.ort.jefud_notifying_system.viewmodel.UsersViewModelFactory
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var vista : View
    private val viewModel:  UsersViewModel by activityViewModels {
        UsersViewModelFactory(
            (activity?.application as JEFUDApplication).database
                .userDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       _binding = FragmentLoginBinding.inflate(inflater, container, false)
        vista = inflater.inflate(R.layout.fragment_login, container, false)
        return binding.root
    }


    override fun onStart() {
        super.onStart()
        addData()
        binding.buttonLogin.setOnClickListener { login() }
    }

    private fun addData() {

        viewModel.addNewUser("44852","password","PANELIST", "CCU")
        viewModel.addNewUser("44","contrasenia","OPERATOR", "CCU")
    }

    private fun login() {
        //viewModel.addNewUser(binding.DNI.text.toString(), binding.password.text.toString(), "PANELISTA")


        if (binding.DNI.text.isNullOrBlank() || binding.password.text.isNullOrBlank()) {
                Toast.makeText(getContext(), "Falta rellenar campos", Toast.LENGTH_SHORT)
                    .show()
        } else {
            viewModel.retrieveUser(binding.DNI.text.toString()).observe(this.viewLifecycleOwner) { selectedUser ->
                val user: User? = selectedUser
                validate(user)
            }
        }

    }

    private fun validate(user: User?) {
        if (user == null) {
            Toast.makeText(getContext(), "Usuario no encontrado", Toast.LENGTH_SHORT)
                .show()
        } else {
            val algorithm = "AES/CBC/PKCS5Padding"
            val key = SecretKeySpec("1234567890123456".toByteArray(), "AES")
            val iv = IvParameterSpec(ByteArray(16))

            val cipherText = encrypt(algorithm, binding.password.text.toString(), key, iv)

            if(decrypt(algorithm, cipherText, key, iv).compareTo(user.password) == 0) {

                val sharedPrefDni = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
                with (sharedPrefDni.edit()) {
                    putString(getString(R.string.saved_userdni_key), user.dni)
                    apply()

                }

                val sharedPrefPanel = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
                with (sharedPrefPanel.edit()) {
                    putString(getString(R.string.saved_userpanel_key), user.panel)
                    apply()

                }

                val password = encrypt(algorithm, user.password, key, iv)

                val sharedPrefPassword = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
                with (sharedPrefPassword.edit()) {
                    putString(getString(R.string.saved_userpassword_key), password)
                    apply()
                }

                val sharedPrefRole = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
                with (sharedPrefRole.edit()) {
                    putString(getString(R.string.saved_userrole_key), user.rol)
                    apply()
                }

                when(user.rol) {
                    "PANELISTA" -> startActivity(Intent(requireContext(), PanelistActivity::class.java))

                }
            } else {
                Toast.makeText(getContext(), "Contraseña no correcta", Toast.LENGTH_SHORT)
                    .show()
            }
        }

    }


    fun decrypt(algorithm: String, cipherText: String, key: SecretKeySpec, iv: IvParameterSpec): String {
        val cipher = Cipher.getInstance(algorithm)
        cipher.init(Cipher.DECRYPT_MODE, key, iv)
        val plainText = cipher.doFinal(Base64.getDecoder().decode(cipherText))
        return String(plainText)
    }

    fun encrypt(algorithm: String, inputText: String, key: SecretKeySpec, iv: IvParameterSpec): String {
        val cipher = Cipher.getInstance(algorithm)
        cipher.init(Cipher.ENCRYPT_MODE, key, iv)
        val cipherText = cipher.doFinal(inputText.toByteArray())
        return Base64.getEncoder().encodeToString(cipherText)
    }




}