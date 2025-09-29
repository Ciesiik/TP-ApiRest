package com.example.tp_apirest

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class LoginActivity : AppCompatActivity() {

    lateinit var etUsuario: EditText
    lateinit var etContraseña: EditText
    lateinit var btnRegistro: Button
    lateinit var btnInicio: Button
    lateinit var cbRecordarUsuario: CheckBox
    lateinit var toolbar: Toolbar

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
        toolbar = findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)
        val titulo = resources.getString(R.string.dezero) + resources.getString(R.string.login)
        supportActionBar!!.title = titulo

        var preferencias = getSharedPreferences(resources.getString(R.string.sp_credenciales), MODE_PRIVATE)
        var usuarioGuardado = preferencias.getString(resources.getString(R.string.nombre), "")
        var passwordGuardado = preferencias.getString(resources.getString(R.string.password), "")

        if(usuarioGuardado!!.isNotEmpty() && passwordGuardado!!.isNotEmpty())
            iniciarActividadPrincipal(usuarioGuardado!!)

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
                login(etUsuario.text.toString(), etContraseña.text.toString())
            }
        }

    }
    private fun iniciarActividadPrincipal(usuario: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("NOMBRE", usuario)
        startActivity(intent)
        finish()
    }

    private fun login(usuario: String, contraseña: String) {
        if(cbRecordarUsuario.isChecked){
            var preferencias = getSharedPreferences(resources.getString(R.string.sp_credenciales), MODE_PRIVATE)
            preferencias.edit().putString(resources.getString(R.string.nombre), usuario).apply()
            preferencias.edit().putString(resources.getString(R.string.password), contraseña).apply()
        }
        iniciarActividadPrincipal(etUsuario.text.toString())
    }
}