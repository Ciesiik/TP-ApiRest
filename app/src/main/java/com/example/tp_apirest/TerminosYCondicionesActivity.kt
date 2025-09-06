package com.example.tp_apirest

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class TerminosYCondicionesActivity : AppCompatActivity() {

    lateinit var btnAceptar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_terminos_ycondiciones)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btnAceptar = findViewById(R.id.btnAceptar)

        var usuario: String? = null

        val bundle: Bundle? = intent.extras
        if(bundle != null){
            usuario = bundle.getString("NOMBRE")
        }

        btnAceptar.setOnClickListener {
            Log.i("TODO", "Registrar la aceptación de los términos")

            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("NOMBRE", usuario)
            startActivity(intent)
            finish()
        }
    }
}