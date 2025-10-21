package com.example.tp_apirest.activities

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.tp_apirest.R
import com.example.tp_apirest.adapters.AlbumsAdapter
import com.example.tp_apirest.adapters.ArtistsAdapter
import com.example.tp_apirest.adapters.TracksAdapter
import com.example.tp_apirest.configurations.RetrofitClient
import com.example.tp_apirest.db.AppDatabase
import com.example.tp_apirest.db.Favorito
import com.example.tp_apirest.db.FavoritoDao
import com.example.tp_apirest.dtos.AlbumDTO
import com.example.tp_apirest.dtos.AlbumListDTO
import com.example.tp_apirest.dtos.ArtistDTO
import com.example.tp_apirest.dtos.ArtistListDTO
import com.example.tp_apirest.dtos.TrackDTO
import com.example.tp_apirest.dtos.TrackListDTO
import com.example.tp_apirest.endpoints.DeezerApi
import com.example.tp_apirest.items.AlbumItem
import com.example.tp_apirest.items.ArtistItem
import com.example.tp_apirest.items.TrackItem
import com.example.tp_apirest.utils.FormatUtils.formatoDuracion
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.concurrent.thread

class SearchActivity : AppCompatActivity() {
    lateinit var toolbar: Toolbar
    lateinit var rvTracks: RecyclerView
    lateinit var tracksAdapter: TracksAdapter
    lateinit var rvAlbums: RecyclerView
    lateinit var albumsAdapter: AlbumsAdapter
    lateinit var rvArtists: RecyclerView
    lateinit var artistsAdapter: ArtistsAdapter
    private lateinit var favoritoDao: FavoritoDao
    private var usuarioId: Int = -1
    private var favoritosIds: MutableSet<Long> = mutableSetOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        toolbar = findViewById(R.id.toolbar)
        rvTracks = findViewById(R.id.rvTracks)
        rvAlbums = findViewById(R.id.rvAlbums)
        rvArtists = findViewById(R.id.rvArtists)
        favoritoDao = AppDatabase.getDatabase(applicationContext).favoritoDao()

        val query = intent.getStringExtra("QUERY") ?: ""

        setSupportActionBar(toolbar)
        val titulo = resources.getString(R.string.resultados) + " ${query}"
        supportActionBar!!.title = titulo

        val prefs = getSharedPreferences(getString(R.string.sp_credenciales), MODE_PRIVATE)
        usuarioId = prefs.getInt(getString(R.string.userid), -1)
        if (usuarioId == -1) {
            Toast.makeText(this, "No hay sesión activa", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val api = RetrofitClient.retrofit.create(DeezerApi::class.java)
        val callSearchTrack = api.searchTrack(query)

        callSearchTrack.enqueue(object : Callback<TrackListDTO> {
            override fun onResponse(
                call: Call<TrackListDTO>,
                response: Response<TrackListDTO>
            ) {
                val tracks = response.body()?.data ?: emptyList()

                thread {
                    favoritosIds = favoritoDao.obtenerFavoritos(usuarioId).toMutableSet()

                    runOnUiThread {
                        val elementos = tracks.map { mapTrackToTrackItem(it) }
                        tracksAdapter = TracksAdapter(
                            elementos,
                            this@SearchActivity,
                            esFavorito = { trackId -> favoritosIds.contains(trackId) },
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

        val callSearchAlbum = api.searchAlbum(query)
        callSearchAlbum.enqueue(object : Callback<AlbumListDTO> {
            override fun onResponse(call: Call<AlbumListDTO>, response: Response<AlbumListDTO>) {
                val albums = response.body()?.data ?: emptyList()
                val elementos = albums.map { mapAlbumToAlbumItem(it) }
                albumsAdapter = AlbumsAdapter(elementos, this@SearchActivity)
                rvAlbums.adapter = albumsAdapter
            }

            override fun onFailure(call: Call<AlbumListDTO>, t: Throwable) {
                Log.e("ERROR", t.message ?: "")
            }
        })

        val callSearchArtist = api.searchArtist(query)
        callSearchArtist.enqueue(object : Callback<ArtistListDTO> {
            override fun onResponse(call: Call<ArtistListDTO>, response: Response<ArtistListDTO>) {
                val artists = response.body()?.data ?: emptyList()
                val elementos = artists.map { mapArtistToArtistItem(it) }
                artistsAdapter = ArtistsAdapter(elementos, this@SearchActivity)
                rvArtists.adapter = artistsAdapter
            }

            override fun onFailure(call: Call<ArtistListDTO>, t: Throwable) {
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

    private fun mapAlbumToAlbumItem(album: AlbumDTO): AlbumItem {
        return AlbumItem(
            id = album.id?: 0,
            titulo = album.title ?: "Sin título",
            coverUrl = album.cover_xl ?: "",
            tipo = album.record_type ?: "Desconocido",
            artista = album.artist?.name ?: "Desconocido"
        )
    }

    private fun mapArtistToArtistItem(artist: ArtistDTO): ArtistItem {
        return ArtistItem(
            id = artist.id?: 0,
            nombre = artist.name ?: "Sin nombre",
            fotoUrl = artist.picture_xl ?: "",
            posicion = artist.position ?: 0,
            numeroAlbums = 0,
            numeroFans = 0
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