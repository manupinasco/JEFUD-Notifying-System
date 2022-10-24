package ar.edu.ort.jefud_notifying_system.view.manager

import android.R
import android.content.Intent
import android.os.Bundle
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.compose.ui.graphics.Color
import androidx.fragment.app.Fragment
import ar.edu.ort.jefud_notifying_system.databinding.FragmentManagerFrecuentAlarmBinding


class ManagerAlarm : Fragment() {

    private var _binding: FragmentManagerFrecuentAlarmBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentManagerFrecuentAlarmBinding.inflate(inflater, container, false)

        val text = "Gran frecuencia de alarmas con tag name" + " PSG1_GE:04PA443CIN. " + "Solucionarlo."
        val ss = SpannableString(text)
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                Toast.makeText(requireContext(),"Hola!",Toast.LENGTH_SHORT).show()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = ds.linkColor
                ds.isUnderlineText = true
            }
        }
        ss.setSpan(clickableSpan, text.length - 13, text.length, Spanned.SPAN_EXCLUSIVE_INCLUSIVE)
        val frecuentAlarmAdviseTextView = binding.frecuentAlarmAdviseTextView
        frecuentAlarmAdviseTextView.text = ss
        frecuentAlarmAdviseTextView.movementMethod = LinkMovementMethod.getInstance()
        frecuentAlarmAdviseTextView.highlightColor = R.color.transparent
        return binding.root
    }
}