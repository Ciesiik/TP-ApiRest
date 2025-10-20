package com.example.tp_apirest.activities

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tp_apirest.R
import com.example.tp_apirest.activities.album.AlbumsActivity
import com.example.tp_apirest.activities.artist.ArtistsActivity
import com.example.tp_apirest.activities.track.TracksActivity
import com.example.tp_apirest.db.AppDatabase
import com.example.tp_apirest.db.FavoritoDao
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity() {

    lateinit var toolbar: Toolbar
    lateinit var btnTracks: Button
    private lateinit var btnAlbums: Button
    private lateinit var btnArtistas: Button
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


        saludarUsuario()

        btnTracks = findViewById(R.id.btnTracks)
        btnAlbums = findViewById(R.id.btnAlbums)
        btnArtistas = findViewById(R.id.btnArtistas)

        btnTracks.setOnClickListener {
            var intent = Intent(this, TracksActivity::class.java)
            startActivity(intent)
        }

        btnAlbums.setOnClickListener {
            var intent = Intent(this, AlbumsActivity::class.java)
            startActivity(intent)
        }

        btnArtistas.setOnClickListener {
            var intent = Intent(this, ArtistsActivity::class.java)
            startActivity(intent)
        }
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

    private fun saludarUsuario() {
        val bundle: Bundle? = intent.extras
        if(bundle != null){
            val usuario = bundle.getString("NOMBRE")
            Toast.makeText(this, "Bienvenido/a $usuario", Toast.LENGTH_SHORT).show()
        }
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
        val preferencias = getSharedPreferences(resources.getString(R.string.sp_credenciales), MODE_PRIVATE)
        preferencias.edit().clear().apply()

        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
