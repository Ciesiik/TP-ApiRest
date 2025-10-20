package com.example.tp_apirest.activities.artist

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.tp_apirest.R
import com.example.tp_apirest.activities.MainActivity
import com.example.tp_apirest.adapters.ArtistsAdapter
import com.example.tp_apirest.configurations.RetrofitClient
import com.example.tp_apirest.dtos.ArtistDTO
import com.example.tp_apirest.dtos.ChartDTO
import com.example.tp_apirest.endpoints.DeezerApi
import com.example.tp_apirest.items.ArtistItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.map

class ArtistsActivity : AppCompatActivity() {

    lateinit var rvItems: RecyclerView
    lateinit var toolbar: Toolbar
    lateinit var artistsAdapter: ArtistsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_artists)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        rvItems = findViewById(R.id.rvItems)
        toolbar = findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        val titleView = TextView(this).apply {
            text = resources.getString(R.string.artists)
            textSize = 20f
            layoutParams = Toolbar.LayoutParams(
                Toolbar.LayoutParams.WRAP_CONTENT,
                Toolbar.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER
            )
        }

        toolbar.addView(titleView)

        val api = RetrofitClient.retrofit.create(DeezerApi::class.java)
        val callPost = api.getChart()

        callPost.enqueue(object : Callback<ChartDTO> {
            override fun onResponse(call: Call<ChartDTO>, response: Response<ChartDTO>) {
                val artists = response.body()?.artists?.data ?: emptyList()
                val elementos = artists.map { mapArtistToArtistItem(it) }
                artistsAdapter = ArtistsAdapter(elementos, this@ArtistsActivity)
                rvItems.adapter = artistsAdapter
                Log.d("Respuesta", elementos.toString())

            }

            override fun onFailure(call: Call<ChartDTO>, t: Throwable) {
                Log.e("ERROR", t.message ?: "")
            }
        })

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