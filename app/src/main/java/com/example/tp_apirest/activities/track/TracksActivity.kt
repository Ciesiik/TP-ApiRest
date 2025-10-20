package com.example.tp_apirest.activities.track

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.tp_apirest.R
import com.example.tp_apirest.activities.MainActivity
import com.example.tp_apirest.adapters.TracksAdapter
import com.example.tp_apirest.configurations.RetrofitClient
import com.example.tp_apirest.db.AppDatabase
import com.example.tp_apirest.db.Favorito
import com.example.tp_apirest.db.FavoritoDao
import com.example.tp_apirest.dtos.ChartDTO
import com.example.tp_apirest.dtos.TrackDTO
import com.example.tp_apirest.endpoints.DeezerApi
import com.example.tp_apirest.items.TrackItem
import com.example.tp_apirest.utils.FormatUtils.formatoDuracion
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.concurrent.thread

class TracksActivity : AppCompatActivity() {

    lateinit var rvItems: RecyclerView

    lateinit var toolbar: Toolbar
    lateinit var tracksAdapter: TracksAdapter
    private var usuarioId: Int = -1
    private var favoritosIds: MutableSet<Long> = mutableSetOf()

    lateinit var favoritoDao: FavoritoDao



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tracks)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        rvItems = findViewById(R.id.rvItems)
        toolbar = findViewById(R.id.toolbar)
        favoritoDao = AppDatabase.getDatabase(applicationContext).favoritoDao()




        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        val titleView = TextView(this).apply {
            text = resources.getString(R.string.tracks)
            textSize = 20f
            layoutParams = Toolbar.LayoutParams(
                Toolbar.LayoutParams.WRAP_CONTENT,
                Toolbar.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER
            )
        }
        toolbar.addView(titleView)


        val prefs = getSharedPreferences(getString(R.string.sp_credenciales), MODE_PRIVATE)
        usuarioId = prefs.getInt(getString(R.string.userid), -1)
        if (usuarioId == -1) {
            Toast.makeText(this, "No hay sesión activa", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val api = RetrofitClient.retrofit.create(DeezerApi::class.java)
        val callChart = api.getChart()
        callChart.enqueue(object : Callback<ChartDTO> {
            override fun onResponse(call: Call<ChartDTO>, response: Response<ChartDTO>) {
                val tracks = response.body()?.tracks?.data ?: emptyList()

                thread {
                    favoritosIds = favoritoDao.obtenerFavoritos(usuarioId).toMutableSet()

                    runOnUiThread {
                        val elementos = tracks.map { mapTrackToTrackItem(it) }
                        tracksAdapter = TracksAdapter(
                            elementos,
                            this@TracksActivity,
                            esFavorito = { trackId -> favoritosIds.contains(trackId) },
                            onToggleFavorito = { track -> toggleFavorito(track) }
                        )
                        rvItems.adapter = tracksAdapter
                        Log.d("Respuesta", elementos.toString())
                    }
                }

            }

            override fun onFailure(call: Call<ChartDTO>, t: Throwable) {
                Log.e("ERROR", t.message ?: "")
            }
        })

    }

    private fun mapTrackToTrackItem(track: TrackDTO): TrackItem {
        return TrackItem(
            id = track.id?: 0,
            titulo = track.title ?: "Sin título",
            autor = track.artist?.name ?: "Desconocido",
            duracion = formatoDuracion(track.duration),
            fecha = "Sin fecha",
            imagenUrl = track.album?.cover_xl ?: ""
        )
    }

    private fun toggleFavorito(track: TrackItem) {
        thread {
            val yaEsFavorito = favoritoDao.esFavorito(usuarioId, track.id)

            if (yaEsFavorito) {
                favoritoDao.eliminarFavorito(usuarioId, track.id)
                favoritosIds.remove(track.id)
            } else {
                favoritoDao.agregarFavorito(Favorito(usuarioId, track.id))
                favoritosIds.add(track.id)
            }

            runOnUiThread {
                tracksAdapter.notifyItemChanged(track.id)
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

    override fun onResume() {
        super.onResume()
        favoritosIds = favoritoDao.obtenerFavoritos(usuarioId).toMutableSet()
        if (::tracksAdapter.isInitialized) {
            tracksAdapter.notifyDataSetChanged()
        }
    }

}