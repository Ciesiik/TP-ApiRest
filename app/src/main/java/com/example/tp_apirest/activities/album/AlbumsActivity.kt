package com.example.tp_apirest.activities.album

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
import com.example.tp_apirest.adapters.AlbumsAdapter
import com.example.tp_apirest.configurations.RetrofitClient
import com.example.tp_apirest.dtos.AlbumDTO
import com.example.tp_apirest.dtos.ChartDTO
import com.example.tp_apirest.endpoints.DeezerApi
import com.example.tp_apirest.items.AlbumItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AlbumsActivity : AppCompatActivity() {
    lateinit var rvItems: RecyclerView
    lateinit var toolbar: Toolbar
    lateinit var albumsAdapter: AlbumsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_albums)
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
            text = resources.getString(R.string.albums)
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
                val albums = response.body()?.albums?.data ?: emptyList()
                val elementos = albums.map { mapAlbumToAlbumItem(it) }
                albumsAdapter = AlbumsAdapter(elementos, this@AlbumsActivity)
                rvItems.adapter = albumsAdapter
                Log.d("Respuesta", elementos.toString())

            }

            override fun onFailure(call: Call<ChartDTO>, t: Throwable) {
                Log.e("ERROR", t.message ?: "")
            }
        })

    }

private fun mapAlbumToAlbumItem(album: AlbumDTO): AlbumItem {
    return AlbumItem(
        id = album.id ?: 0,
        titulo = album.title ?: "Sin título",
        coverUrl = album.cover_xl ?: "",
        tipo = album.record_type ?: "Desconocido",
        artista = album.artist?.name ?: "Desconocido"
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