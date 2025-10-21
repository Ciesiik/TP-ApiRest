package com.example.tp_apirest.activities

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tp_apirest.R
import com.example.tp_apirest.db.AppDatabase
import com.example.tp_apirest.db.FavoritoDao
import com.example.tp_apirest.fragmentos.FragmentoBusqueda
import com.example.tp_apirest.fragmentos.PrimerFragmento
import com.example.tp_apirest.fragmentos.PrimerFragmentoInterfaz
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity(), PrimerFragmentoInterfaz {

    lateinit var toolbar: Toolbar
    private lateinit var favoritoDao: FavoritoDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val primerFragmento = supportFragmentManager
            .findFragmentById(R.id.contenedor_primer_fragmento) as? PrimerFragmento
        primerFragmento?.listener = this


        toolbar = findViewById(R.id.toolbar)
        favoritoDao = AppDatabase.getDatabase(applicationContext).favoritoDao()

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        val titleView = TextView(this).apply {
            text = resources.getString(R.string.dezero)
            textSize = 20f
            layoutParams = Toolbar.LayoutParams(
                Toolbar.LayoutParams.WRAP_CONTENT,
                Toolbar.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER
            )
        }
        toolbar.addView(titleView)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.item_cerrar_sesion){
            cerrarSesion()
        } else if(item.itemId == R.id.item_ver_favoritos){
            verFavoritos()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun verFavoritos() {
        val prefs = getSharedPreferences(getString(R.string.sp_credenciales), MODE_PRIVATE)
        val usuarioId = prefs.getInt(getString(R.string.userid), -1)
        thread {
            val favoritosIds = favoritoDao.obtenerFavoritos(usuarioId)

            runOnUiThread {
                if (favoritosIds.isEmpty()) {
                    Toast.makeText(this, "No tenés favoritos guardados", Toast.LENGTH_SHORT).show()
                } else {
                    val intent = Intent(this, FavoritosActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }


    private fun cerrarSesion() {
        val dialog = AlertDialog.Builder(this, R.style.MiAlertDialog)
            .setMessage("¿Seguro que quieres cerrar sesión?")
            .setPositiveButton("CERRAR SESIÓN") { _, _ ->
                val preferencias = getSharedPreferences(
                    resources.getString(R.string.sp_credenciales),
                    MODE_PRIVATE
                )
                preferencias.edit().clear().apply()

                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
            .setNegativeButton("CANCELAR", null)
            .show()

        dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setTextColor(ContextCompat.getColor(this, R.color.amarillo))
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.setTextColor(ContextCompat.getColor(this, R.color.white))
    }

    override fun mostrarBusqueda(){
        val yaVisible = supportFragmentManager.findFragmentByTag("fragmento_busqueda") != null
        if (!yaVisible) {
            val fragmentoBusqueda = FragmentoBusqueda()
            supportFragmentManager.beginTransaction()
                .replace(R.id.contenedor_fragmento_busqueda, fragmentoBusqueda, "fragmento_busqueda")
                .addToBackStack(null)
                .commit()
        }
    }
}
