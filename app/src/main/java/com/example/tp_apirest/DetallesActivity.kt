package com.example.tp_apirest

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class DetallesActivity : AppCompatActivity() {

    lateinit var ivPortada: ImageView
    lateinit var tvTitulo: TextView
    lateinit var tvAutor: TextView
    lateinit var tvDuracion: TextView
    lateinit var tvFecha: TextView
    lateinit var tvDescripcion: TextView
    lateinit var btnReproducir: Button
    lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detalles)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        ivPortada = findViewById(R.id.ivPortada)
        tvTitulo = findViewById(R.id.tvTitulo)
        tvAutor = findViewById(R.id.tvAutor)
        tvDuracion = findViewById(R.id.tvDuracion)
        tvFecha = findViewById(R.id.tvFecha)
        tvDescripcion = findViewById(R.id.tvDescripcion)
        btnReproducir = findViewById(R.id.btnReproducir)
        toolbar = findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)
        val titulo = resources.getString(R.string.app_name) + resources.getString(R.string.details)
        supportActionBar!!.title = titulo

        val bundle: Bundle? = intent.extras
        if(bundle != null){
            tvTitulo.text = resources.getString(R.string.titulo) + " " + bundle.getString("TITULO")
            tvAutor.text = resources.getString(R.string.autor) + " " + bundle.getString("AUTOR")
            tvDuracion.text = resources.getString(R.string.duracion) + " " + bundle.getString("DURACION")
            tvFecha.text = resources.getString(R.string.fecha_lanzamiento) + " " + bundle.getString("FECHA")
            tvDescripcion.text = bundle.getString("DESCRIPCION")

            val imagenId = intent.getIntExtra("IMAGEN", R.drawable.placeholder)
            ivPortada.setImageResource(obtenerImagenPorId(imagenId))


            var youtubeUrl = bundle.getString("YOUTUBEURL")


            btnReproducir.setOnClickListener {
                if (!youtubeUrl.isNullOrEmpty()) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeUrl))
                    intent.setPackage("com.google.android.youtube")
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "No hay enlace disponible", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun obtenerImagenPorId(id: Int): Int {
        return when (id) {
            1 -> R.drawable.blurryface
            2 -> R.drawable.trench
            3 -> R.drawable.vessel
            4 -> R.drawable.heathens
            5 -> R.drawable.am
            6 -> R.drawable.fwn
            7 -> R.drawable.whatever_people
            8 -> R.drawable.the_car
            9 -> R.drawable.demon_days
            10 -> R.drawable.gorillaz
            11 -> R.drawable.plastic_beach
            12 -> R.drawable.cracker_island
            13 -> R.drawable.room_on_fire
            14 -> R.drawable.is_this_it
            15 -> R.drawable.angles
            16 -> R.drawable.the_new_abnormal
            17 -> R.drawable.sintesis_o_konor
            18 -> R.drawable.dia_de_muertos
            19 -> R.drawable.dinastia_scorpio
            20 -> R.drawable.humanz
            else -> R.drawable.placeholder
        }
    }
}