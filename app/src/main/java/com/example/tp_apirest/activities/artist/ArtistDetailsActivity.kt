package com.example.tp_apirest.activities.artist

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
import com.bumptech.glide.Glide
import com.example.tp_apirest.R
import com.example.tp_apirest.adapters.AlbumsAdapter
import com.example.tp_apirest.adapters.TracksAdapter
import com.example.tp_apirest.configurations.RetrofitClient
import com.example.tp_apirest.db.AppDatabase
import com.example.tp_apirest.db.Favorito
import com.example.tp_apirest.db.FavoritoDao
import com.example.tp_apirest.dtos.AlbumDTO
import com.example.tp_apirest.dtos.AlbumListDTO
import com.example.tp_apirest.dtos.ArtistDetailDTO
import com.example.tp_apirest.dtos.TrackDTO
import com.example.tp_apirest.dtos.TrackListDTO
import com.example.tp_apirest.endpoints.DeezerApi
import com.example.tp_apirest.items.AlbumItem
import com.example.tp_apirest.items.TrackItem
import com.example.tp_apirest.utils.FormatUtils.formatoDuracion
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.concurrent.thread

class ArtistDetailsActivity : AppCompatActivity() {
    lateinit var toolbar: Toolbar
    lateinit var imgFotoArtista: CircleImageView
    lateinit var tvNombreArtista: TextView
    lateinit var tvSeguidores: TextView
    lateinit var rvAlbums: RecyclerView
    lateinit var rvTopTracks: RecyclerView
    lateinit var albumsAdapter: AlbumsAdapter
    lateinit var tracksAdapter: TracksAdapter
    private lateinit var favoritoDao: FavoritoDao
    private var usuarioId: Int = -1
    private var favoritosIds: MutableSet<Long> = mutableSetOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_artist_detail)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        imgFotoArtista = findViewById(R.id.imgFotoArtista)
        tvNombreArtista = findViewById(R.id.tvNombreArtista)
        tvSeguidores = findViewById(R.id.tvSeguidores)
        rvAlbums = findViewById(R.id.rvAlbumes)
        rvTopTracks = findViewById(R.id.rvTopTracks)
        favoritoDao = AppDatabase.getDatabase(applicationContext).favoritoDao()
        toolbar = findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        val titleView = TextView(this).apply {
            text = resources.getString(R.string.artist) + " " + resources.getString(R.string.details)
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
        var artistId: Long? = 0L

        val bundle: Bundle? = intent.extras
        if(bundle != null){
            artistId = bundle.getLong("ARTIST_ID")
        }

        val api = RetrofitClient.retrofit.create(DeezerApi::class.java)
        val callArtDetail = api.getArtist(artistId!!)

        callArtDetail.enqueue(object : Callback<ArtistDetailDTO> {
            override fun onResponse(
                call: Call<ArtistDetailDTO>,
                response: Response<ArtistDetailDTO>
            ) {
                val artist = response.body() ?: return
                Glide.with(this@ArtistDetailsActivity)
                    .load(artist.picture_xl)
                    .placeholder(R.drawable.placeholder)
                    .into(imgFotoArtista)

                tvNombreArtista.text = artist.name ?: "Unkwown"

                tvSeguidores.text = "${artist.nb_fan ?: 0} seguidores"

                val callAlbums = api.getArtistAlbums(artist.id!!)
                callAlbums.enqueue(object : Callback<AlbumListDTO> {
                    override fun onResponse(call: Call<AlbumListDTO>, response: Response<AlbumListDTO>) {
                        val albums = response.body()?.data ?: emptyList()
                        val elementos = albums.map { mapAlbumToAlbumItem(it, artist.name ?: "Desconocido") }
                        albumsAdapter = AlbumsAdapter(elementos, this@ArtistDetailsActivity)
                        rvAlbums.adapter = albumsAdapter
                    }

                    override fun onFailure(call: Call<AlbumListDTO>, t: Throwable) {
                        Log.e("AlbumError", t.message ?: "")
                    }
                })


                val tracklistUrl: String?
                tracklistUrl = artist.tracklist

                if (!tracklistUrl.isNullOrEmpty()) {
                    val callTopTracks = api.getArtistTopTracks(tracklistUrl)
                    callTopTracks.enqueue(object : Callback<TrackListDTO> {
                        override fun onResponse(
                            call: Call<TrackListDTO>,
                            response: Response<TrackListDTO>
                        ) {
                            val tracks = response.body()?.data ?: emptyList()

                            thread {
                                favoritosIds =
                                    favoritoDao.obtenerFavoritos(usuarioId).toMutableSet()

                                runOnUiThread {
                                    val elementos =
                                        tracks.map { mapTrackToTrackItem(it) }

                                    tracksAdapter = TracksAdapter(
                                        elementos,
                                        this@ArtistDetailsActivity,
                                        esFavorito = { trackId: Long ->
                                            favoritosIds.contains(
                                                trackId
                                            )
                                        },
                                        onToggleFavorito = { track -> toggleFavorito(track) }
                                    )
                                    rvTopTracks.adapter = tracksAdapter
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

            override fun onFailure(call: Call<ArtistDetailDTO>, t: Throwable) {
                Log.e("AlbumDetails", "Error: ${t.message}")
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

    private fun mapAlbumToAlbumItem(album: AlbumDTO, artista: String?): AlbumItem {
        return AlbumItem(
            id = album.id?: 0,
            titulo = album.title ?: "Sin título",
            coverUrl = album.cover_xl ?: "",
            tipo = album.record_type ?: "Desconocido",
            artista = artista ?: ""
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