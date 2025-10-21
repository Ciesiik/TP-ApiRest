package com.example.tp_apirest.fragmentos

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.example.tp_apirest.R
import com.example.tp_apirest.activities.SearchActivity

class FragmentoBusqueda: Fragment() {
    lateinit var btnCerrarBusqueda: ImageButton
    lateinit var btnLimpiarBUsqueda: ImageButton
    lateinit var etBusqueda: EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragmento_busqueda, container, false)
        btnCerrarBusqueda = view.findViewById(R.id.btnCerrarBusqueda)
        btnLimpiarBUsqueda = view.findViewById(R.id.btnLimpiarBusqueda)
        etBusqueda = view.findViewById(R.id.etBuscar)
        btnCerrarBusqueda.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        btnLimpiarBUsqueda.setOnClickListener {
            etBusqueda.text.clear()
        }

        etBusqueda.setOnEditorActionListener { _, actionId, event ->
            val isEnter = actionId == EditorInfo.IME_ACTION_SEARCH ||
                    (event?.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)

            if (isEnter) {
                val texto = etBusqueda.text.toString().trim()
                if (texto.isNotEmpty()) {
                    val intent = Intent(requireContext(), SearchActivity::class.java)
                    intent.putExtra("QUERY", texto)
                    startActivity(intent)
                }
                true
            } else {
                false // no lo consumimos
            }
        }


        return view
    }
}