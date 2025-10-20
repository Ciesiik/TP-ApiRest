package com.example.tp_apirest.activities.album

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tp_apirest.R
import com.example.tp_apirest.activities.artist.ArtistDetailsActivity
import com.example.tp_apirest.adapters.TracksAdapter
import com.example.tp_apirest.configurations.RetrofitClient
import com.example.tp_apirest.db.AppDatabase
import com.example.tp_apirest.db.Favorito
import com.example.tp_apirest.db.FavoritoDao
import com.example.tp_apirest.dtos.AlbumDetailDTO
import com.example.tp_apirest.dtos.TrackDTO
import com.example.tp_apirest.dtos.TrackListDTO
import com.example.tp_apirest.endpoints.DeezerApi
import com.example.tp_apirest.items.TrackItem
import com.example.tp_apirest.utils.FormatUtils
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.concurrent.thread

class AlbumDetailsActivity : AppCompatActivity() {

    lateinit var toolbar: Toolbar
    lateinit var tvTituloAlbum: TextView
    lateinit var tvAnioTipo: TextView
    lateinit var imgCoverAlbum: ImageView
    lateinit var tvTracksDuracion: TextView
    lateinit var contenedorArtista: LinearLayout
    lateinit var imgFotoArtista: CircleImageView
    lateinit var tvNombreArtista: TextView
    lateinit var tvGeneros: TextView
    lateinit var tvLabel: TextView
    lateinit var tvFans: TextView
    lateinit var rvTracks: RecyclerView
    lateinit var tracksAdapter: TracksAdapter
    private lateinit var favoritoDao: FavoritoDao
    private var usuarioId: Int = -1
    private var favoritosIds: MutableSet<Long> = mutableSetOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_album_details)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        tvTituloAlbum = findViewById(R.id.tvTituloAlbum)
        tvAnioTipo = findViewById(R.id.tvAnioTipo)
        imgCoverAlbum = findViewById(R.id.imgCoverAlbum)
        tvTracksDuracion = findViewById(R.id.tvTracksDuracion)
        contenedorArtista = findViewById(R.id.contenedorArtista)
        imgFotoArtista = findViewById(R.id.imgFotoArtista)
        tvNombreArtista = findViewById(R.id.tvNombreArtista)
        tvGeneros = findViewById(R.id.tvGeneros)
        tvLabel = findViewById(R.id.tvLabel)
        tvFans = findViewById(R.id.tvFans)
        rvTracks = findViewById(R.id.rvTracks)
        toolbar = findViewById(R.id.toolbar)
        favoritoDao = AppDatabase.Companion.getDatabase(applicationContext).favoritoDao()



        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        val titleView = TextView(this).apply {
            text = resources.getString(R.string.album) + " " + resources.getString(R.string.details)
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

        var albumId: Long? = 0L

        val bundle: Bundle? = intent.extras
        if (bundle != null) {
            albumId = bundle.getLong("ALBUM_ID")
        }

        val api = RetrofitClient.retrofit.create(DeezerApi::class.java)
        val callAlbumDetail = api.getAlbum(albumId!!)

        callAlbumDetail.enqueue(object : Callback<AlbumDetailDTO> {
            override fun onResponse(
                call: Call<AlbumDetailDTO>,
                response: Response<AlbumDetailDTO>
            ) {
                val album = response.body() ?: return

                tvTituloAlbum.text = album.title ?: "Unkwown"

                val releaseDate = album.release_date
                val recordType = album.record_type
                val anio = releaseDate?.substringBefore("-") ?: "--"
                val tipo = (recordType ?: "--").uppercase()
                val anioTipo = "$anio · $tipo"

                tvAnioTipo.text = anioTipo

                Glide.with(this@AlbumDetailsActivity)
                    .load(album.cover_xl)
                    .placeholder(R.drawable.placeholder)
                    .into(imgCoverAlbum)

                val nroCanciones = album.nb_tracks ?: "-"
                val duracion = FormatUtils.formatoDuracion(album.duration)

                tvTracksDuracion.text = "$nroCanciones canciones · $duracion"

                tvNombreArtista.text = album.artist?.name ?: "-"

                Glide.with(this@AlbumDetailsActivity)
                    .load(album.artist?.picture_xl)
                    .placeholder(R.drawable.placeholder)
                    .into(imgFotoArtista)

                contenedorArtista.setOnClickListener {
                    val intent = Intent(this@AlbumDetailsActivity, ArtistDetailsActivity::class.java)
                    intent.putExtra(
                        "ARTIST_ID",
                        album.artist?.id ?: return@setOnClickListener
                    )
                    startActivity(intent)
                }

                val generosTexto = album.genres?.data
                    ?.mapNotNull { it.name }
                    ?.joinToString(", ")
                    ?: "Sin género definido"

                tvGeneros.text = resources.getString(R.string.generos) + " ${generosTexto}"

                tvLabel.text =
                    resources.getString(R.string.discografica) + " ${album.label ?: "--"}"

                tvFans.text = resources.getString(R.string.fans) + " ${album.fans ?: "--"}"

                val tracklistUrl: String?
                tracklistUrl = album.tracklist

                if (!tracklistUrl.isNullOrEmpty()) {
                    val callAlbumTracklist = api.getAlbumTracklist(tracklistUrl)
                    callAlbumTracklist.enqueue(object : Callback<TrackListDTO> {
                        override fun onResponse(
                            call: Call<TrackListDTO>,
                            response: Response<TrackListDTO>
                        ) {
                            val tracks = response.body()?.data ?: emptyList()

                            thread {
                                favoritosIds =
                                    favoritoDao.obtenerFavoritos(usuarioId).toMutableSet()

                                val albumCover = album.cover_xl

                                runOnUiThread {
                                    val elementos =
                                        tracks.map { mapTrackToTrackItem(it, albumCover) }

                                    tracksAdapter = TracksAdapter(
                                        elementos,
                                        this@AlbumDetailsActivity,
                                        esFavorito = { trackId: Long ->
                                            favoritosIds.contains(
                                                trackId
                                            )
                                        },
                                        onToggleFavorito = { track -> toggleFavorito(track) }
                                    )
                                    rvTracks.adapter = tracksAdapter
                                    Log.d("Respuesta", elementos.toString())
                                }
                            }

                        }

                        override fun onFailure(call: Call<TrackListDTO>, t: Throwable) {
                            Log.e("ERROR", t.message ?: "")
                        }


                    })
                }
            }

            override fun onFailure(call: Call<AlbumDetailDTO>, t: Throwable) {
                Log.e("AlbumDetails", "Error: ${t.message}")
            }
        })

    }

    private fun mapTrackToTrackItem(track: TrackDTO, albumCover: String?): TrackItem {
        return TrackItem(
            id = track.id ?: 0,
            titulo = track.title ?: "Sin título",
            autor = track.artist?.name ?: "Desconocido",
            duracion = FormatUtils.formatoDuracion(track.duration),
            fecha = "Sin fecha",
            imagenUrl = albumCover ?: ""
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
            onBackPressedDispatcher.onBackPressed()
            return true
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