package com.example.tp_apirest.activities

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.tp_apirest.R
import com.example.tp_apirest.adapters.TracksAdapter
import com.example.tp_apirest.configurations.RetrofitClient
import com.example.tp_apirest.db.AppDatabase
import com.example.tp_apirest.db.FavoritoDao
import com.example.tp_apirest.endpoints.DeezerApi
import com.example.tp_apirest.items.TrackItem
import com.example.tp_apirest.utils.FormatUtils.formatoDuracion
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.concurrent.thread

class FavoritosActivity : AppCompatActivity() {
    lateinit var toolbar: Toolbar
    private lateinit var rvFavoritos: RecyclerView
    private lateinit var tracksAdapter: TracksAdapter
    private lateinit var favoritoDao: FavoritoDao
    private var usuarioId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_favoritos)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        toolbar = findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        val titleView = TextView(this).apply {
            text = resources.getString(R.string.favoritos)
            textSize = 20f
            layoutParams = Toolbar.LayoutParams(
                Toolbar.LayoutParams.WRAP_CONTENT,
                Toolbar.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER
            )
        }
        toolbar.addView(titleView)
        rvFavoritos = findViewById(R.id.rvFavoritos)
        favoritoDao = AppDatabase.getDatabase(applicationContext).favoritoDao()

        val prefs = getSharedPreferences(getString(R.string.sp_credenciales), MODE_PRIVATE)
        usuarioId = prefs.getInt(getString(R.string.userid), -1)

        if (usuarioId == -1) {
            finish()
            return
        }

        cargarFavoritos()
    }

    private fun cargarFavoritos() {
        lifecycleScope.launch {
            val favoritosIds = withContext(Dispatchers.IO) {
                favoritoDao.obtenerFavoritos(usuarioId)
            }

            val api = RetrofitClient.retrofit.create(DeezerApi::class.java)

            val tracks = favoritosIds.map { id ->
                async(Dispatchers.IO) {
                    try {
                        api.getTrack(id).execute().body()
                    } catch (e: Exception) {
                        null
                    }
                }
            }.awaitAll().filterNotNull()

            val elementos = tracks.map { track ->
                TrackItem(
                    id = track.id ?: 0,
                    titulo = track.title ?: "Sin título",
                    autor = track.artist?.name ?: "Desconocido",
                    duracion = formatoDuracion(track.duration),
                    fecha = "Sin fecha",
                    imagenUrl = track.album?.cover_xl ?: ""
                )
            }

            tracksAdapter = TracksAdapter(
                elementos,
                this@FavoritosActivity,
                esFavorito = { true },
                onToggleFavorito = { track -> toggleFavorito(track) }
            )
            rvFavoritos.adapter = tracksAdapter
        }
    }


    private fun toggleFavorito(track: TrackItem) {
        thread {
            favoritoDao.eliminarFavorito(usuarioId, track.id)
            runOnUiThread {
                cargarFavoritos() // recargar lista
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_back, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.item_back) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}