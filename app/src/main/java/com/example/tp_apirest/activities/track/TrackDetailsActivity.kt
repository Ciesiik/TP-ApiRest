package com.example.tp_apirest.activities.track

import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.tp_apirest.R
import com.example.tp_apirest.activities.album.AlbumDetailsActivity
import com.example.tp_apirest.activities.artist.ArtistDetailsActivity
import com.example.tp_apirest.configurations.RetrofitClient
import com.example.tp_apirest.db.AppDatabase
import com.example.tp_apirest.db.Favorito
import com.example.tp_apirest.dtos.TrackDetailDTO
import com.example.tp_apirest.endpoints.DeezerApi
import com.example.tp_apirest.utils.FormatUtils.formatoDuracion
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.concurrent.thread

class TrackDetailsActivity : AppCompatActivity() {
    private var mediaPlayer: MediaPlayer? = null
    private var reproduciendo = false

    lateinit var toolbar: Toolbar
    lateinit var tvTrackTitulo: TextView
    lateinit var imgAlbumCover: ImageView
    lateinit var tvAlbumTitulo: TextView
    lateinit var tvDuracion: TextView
    lateinit var tvGanancia: TextView
    lateinit var imgFotoArtista: CircleImageView
    lateinit var tvNombreArtista: TextView
    lateinit var tvRank: TextView
    lateinit var tvFecha: TextView
    lateinit var tvTrackNumero: TextView
    lateinit var ibPlayPause: ImageButton
    lateinit var ibFavorito: ImageButton

    lateinit var contenedorArtista: LinearLayout
    lateinit var contenedorAlbum: LinearLayout




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_track_details)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        tvTrackTitulo = findViewById(R.id.tvTrackTitulo)
        imgAlbumCover = findViewById(R.id.imgAlbumCover)
        tvAlbumTitulo = findViewById(R.id.tvAlbumTitulo)
        tvDuracion = findViewById(R.id.tvDuracion)
        tvFecha = findViewById(R.id.tvFecha)
        tvGanancia = findViewById(R.id.tvGanancia)
        imgFotoArtista = findViewById(R.id.imgFotoArtista)
        tvNombreArtista = findViewById(R.id.tvNombreArtista)
        tvTrackNumero = findViewById(R.id.tvTrackNumero)
        tvRank = findViewById(R.id.tvRank)
        ibPlayPause = findViewById(R.id.ibPlayPause)
        ibFavorito = findViewById(R.id.ibFavorito)
        contenedorArtista = findViewById(R.id.contenedorArtista)
        contenedorAlbum = findViewById(R.id.contenedorAlbum)

        toolbar = findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        val titleView = TextView(this).apply {
            text = resources.getString(R.string.track) +" "+resources.getString(R.string.details)
            textSize = 20f
            layoutParams = Toolbar.LayoutParams(
                Toolbar.LayoutParams.WRAP_CONTENT,
                Toolbar.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER
            )
        }
        toolbar.addView(titleView)



        var trackId: Long? = 0L

        val bundle: Bundle? = intent.extras
        if(bundle != null){
            trackId = bundle.getLong("TRACK_ID")
        }

        val api = RetrofitClient.retrofit.create(DeezerApi::class.java)
        val callPost = api.getTrack(trackId!!)

        callPost.enqueue(object : Callback<TrackDetailDTO> {
            override fun onResponse(call: Call<TrackDetailDTO>, response: Response<TrackDetailDTO>) {
                val track = response.body()?: return
                tvTrackTitulo.text = track.title
                tvDuracion.text = resources.getString(R.string.duracion) + " ${formatoDuracion(track.duration)}"
                tvGanancia.text = resources.getString(R.string.ganancia) + " ${track.gain ?: "-"} db"
                tvRank.text = resources.getString(R.string.ranking) + " ${track.rank ?: "--"}"
                tvTrackNumero.text = resources.getString(R.string.trackNumero) + " ${track.track_position ?: "-"}"
                tvFecha.text = resources.getString(R.string.fecha_lanzamiento) + " ${track.release_date ?: "--"}"
                tvNombreArtista.text = track.artist?.name ?: "-"

                Glide.with(this@TrackDetailsActivity)
                    .load(track.artist?.picture_xl)
                    .placeholder(R.drawable.placeholder)
                    .into(imgFotoArtista)

                contenedorArtista.setOnClickListener {
                    val intent = Intent(this@TrackDetailsActivity, ArtistDetailsActivity::class.java)
                      intent.putExtra(
                       "ARTIST_ID",
                       track.artist?.id ?: return@setOnClickListener
                        )
                            startActivity(intent)
                }

                // Álbum
                tvAlbumTitulo.text = track.album?.title ?: "-"
                Glide.with(this@TrackDetailsActivity)
                    .load(track.album?.cover_xl)
                    .placeholder(R.drawable.placeholder)
                    .into(imgAlbumCover)

                contenedorAlbum.setOnClickListener {
                    val intent = Intent(this@TrackDetailsActivity, AlbumDetailsActivity::class.java)
                    intent.putExtra(
                        "ALBUM_ID",
                        track.album?.id ?: return@setOnClickListener
                    )
                    startActivity(intent)
                }

                // preview
                ibPlayPause.setOnClickListener {
                    val previewUrl = track.preview
                    if (!previewUrl.isNullOrEmpty()) {
                        if (reproduciendo) {
                            detenerPreview()
                        } else {
                            reproducirPreview(previewUrl)
                        }
                    } else {
                        Toast.makeText(
                            this@TrackDetailsActivity,
                            "No hay preview disponible",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                val prefs = getSharedPreferences(getString(R.string.sp_credenciales), MODE_PRIVATE)
                val usuarioId = prefs.getInt(getString(R.string.userid), -1)

                val favoritoDao = AppDatabase.getDatabase(applicationContext).favoritoDao()

                thread {
                    val esFavorito = favoritoDao.esFavorito(usuarioId, trackId)
                    runOnUiThread {
                        val icono =
                            if (esFavorito) R.drawable.favorito else R.drawable.no_favorito
                        ibFavorito.setImageResource(icono)
                    }
                }

                ibFavorito.setOnClickListener {
                    thread {
                        val yaEsFavorito = favoritoDao.esFavorito(usuarioId, trackId)

                        if (yaEsFavorito) {
                            favoritoDao.eliminarFavorito(usuarioId, trackId)
                        } else {
                            favoritoDao.agregarFavorito(Favorito(usuarioId, trackId))
                        }

                        runOnUiThread {
                            val nuevoIcono =
                                if (yaEsFavorito) R.drawable.no_favorito else R.drawable.favorito
                            ibFavorito.setImageResource(nuevoIcono)
                        }
                    }

                }

            }

            override fun onFailure(call: Call<TrackDetailDTO>, t: Throwable) {
                Log.e("TrackDetails", "Error: ${t.message}")
            }
        })

    }

    fun reproducirPreview(url: String) {
        detenerPreview()
        mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
            setDataSource(url)
            prepareAsync()
            setOnPreparedListener {
                start()
                reproduciendo = true
                ibPlayPause.setImageResource(R.drawable.pause)
            }
            setOnCompletionListener {
                ibPlayPause.setImageResource(R.drawable.play)
                reproduciendo = false
            }
            setOnErrorListener { _, _, _ ->
                reproduciendo = false
                ibPlayPause.setImageResource(R.drawable.play)
                Toast.makeText(this@TrackDetailsActivity, "Error al reproducir preview", Toast.LENGTH_SHORT).show()
                true
            }
        }
    }

    fun detenerPreview() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        reproduciendo = false
        ibPlayPause.setImageResource(R.drawable.play)
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.stop()
        mediaPlayer = null
    }

    override fun onPause() {
        super.onPause()
        detenerPreview()
    }


        override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_back, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.item_back) {
            onBackPressedDispatcher.onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }


}