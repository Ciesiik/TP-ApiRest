package com.example.tp_apirest

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class LoginActivity : AppCompatActivity() {

    lateinit var etUsuario: EditText
    lateinit var etContraseña: EditText
    lateinit var btnRegistro: Button
    lateinit var btnInicio: Button
    lateinit var cbRecordarUsuario: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        etUsuario = findViewById(R.id.etUsuario)
        etContraseña = findViewById(R.id.etContraseña)
        btnRegistro = findViewById(R.id.btnRegistro)
        btnInicio = findViewById(R.id.btnInicio)
        cbRecordarUsuario = findViewById(R.id.cbRecordarUsuario)

        btnRegistro.setOnClickListener {
            if(etUsuario.text.toString().isEmpty() || etContraseña.text.toString().isEmpty()){
                Toast.makeText(this, "Completar datos", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, TerminosYCondicionesActivity::class.java)
                intent.putExtra("NOMBRE", etUsuario.text.toString())
                startActivity(intent)
                finish()
            }
        }

        btnInicio.setOnClickListener {

            if(etUsuario.text.toString().isEmpty() || etContraseña.text.toString().isEmpty()){
                Toast.makeText(this, "Completar datos", Toast.LENGTH_SHORT).show()
            } else{
                if(cbRecordarUsuario.isChecked)
                    Log.i("TODO", "Funcionalidad de Recordar Usuario")

                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("NOMBRE", etUsuario.text.toString())
                startActivity(intent)
                finish()
            }
        }

    }
}