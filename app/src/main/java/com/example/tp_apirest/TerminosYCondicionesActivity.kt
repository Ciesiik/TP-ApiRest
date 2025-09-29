package com.example.tp_apirest

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.concurrent.thread

class TerminosYCondicionesActivity : AppCompatActivity() {

    lateinit var btnAceptar: Button
    lateinit var toolbar: Toolbar

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
        toolbar = findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)
        val titulo = resources.getString(R.string.dezero) + resources.getString(R.string.terms)
        supportActionBar!!.title = titulo

        var nombre: String? = null
        var contraseña: String? = null

        val bundle: Bundle? = intent.extras
        if(bundle != null){
            nombre = bundle.getString("NOMBRE")
            contraseña = bundle.getString("CONTRASEÑA")
        }

        btnAceptar.setOnClickListener {
            thread {
                var nuevoUsuario = Usuario(nombre!!, contraseña!!)
                AppDatabase.getDatabase(applicationContext).usuarioDao().insert(nuevoUsuario)
                    runOnUiThread {
                        Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, MainActivity::class.java)
                        intent.putExtra("NOMBRE", nombre)
                        startActivity(intent)
                        finish()
                    }
            }
        }
    }
}