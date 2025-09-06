package com.example.tp_apirest

import Elemento
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.dezero.ElementosAdapter

class MainActivity : AppCompatActivity() {

    lateinit var rvElementos: RecyclerView
    lateinit var elementosAdapter: ElementosAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        rvElementos = findViewById(R.id.rvElementos)
        elementosAdapter = ElementosAdapter(getElementos(), this)
        rvElementos.adapter = elementosAdapter


    }

    private fun getElementos(): MutableList<Elemento> {
        var elementos: MutableList<Elemento> = ArrayList()
        elementos.add(Elemento(id = 1, titulo = "Stressed Out", autor = "Twenty One Pilots", fecha = "2015"))
        elementos.add(Elemento(id = 2, titulo = "Ride", autor = "Twenty One Pilots", fecha = "2015"))
        elementos.add(Elemento(id = 3, titulo = "Chlorine", autor = "Twenty One Pilots", fecha = "2018"))
        elementos.add(Elemento(id = 4, titulo = "Car Radio", autor = "Twenty One Pilots", fecha = "2012"))
        elementos.add(Elemento(id = 5, titulo = "Heathens", autor = "Twenty One Pilots", fecha = "2016"))
        elementos.add(Elemento(id = 6, titulo = "Do I Wanna Know?", autor = "Arctic Monkeys", fecha = "2013"))
        elementos.add(Elemento(id = 7, titulo = "505", autor = "Arctic Monkeys", fecha = "2007"))
        elementos.add(Elemento(id = 8, titulo = "I Bet You Look Good on the Dancefloor", autor = "Arctic Monkeys", fecha = "2005"))
        elementos.add(Elemento(id = 9, titulo = "Why'd You Only Call Me When You're High?", autor = "Arctic Monkeys", fecha = "2013"))
        elementos.add(Elemento(id = 10, titulo = "There’d Better Be a Mirrorball", autor = "Arctic Monkeys", fecha = "2022"))
        elementos.add(Elemento(id = 11, titulo = "Feel Good Inc.", autor = "Gorillaz", fecha = "2005"))
        elementos.add(Elemento(id = 12, titulo = "Clint Eastwood", autor = "Gorillaz", fecha = "2001"))
        elementos.add(Elemento(id = 13, titulo = "On Melancholy Hill", autor = "Gorillaz", fecha = "2010"))
        elementos.add(Elemento(id = 14, titulo = "Cracker Island", autor = "Gorillaz ft. Thundercat", fecha = "2022"))
        elementos.add(Elemento(id = 15, titulo = "Saturnz Barz", autor = "Gorillaz", fecha = "2017"))
        elementos.add(Elemento(id = 16, titulo = "Reptilia", autor = "The Strokes", fecha = "2003"))
        elementos.add(Elemento(id = 17, titulo = "Last Nite", autor = "The Strokes", fecha = "2001"))
        elementos.add(Elemento(id = 18, titulo = "Someday", autor = "The Strokes", fecha = "2001"))
        elementos.add(Elemento(id = 19, titulo = "Under Cover of Darkness", autor = "The Strokes", fecha = "2011"))
        elementos.add(Elemento(id = 20, titulo = "At The Door", autor = "The Strokes", fecha = "2020"))
        elementos.add(Elemento(id = 21, titulo = "El Tesoro", autor = "Él Mató a un Policía Motorizado", fecha = "2017"))
        elementos.add(Elemento(id = 22, titulo = "La Noche Eterna", autor = "Él Mató a un Policía Motorizado", fecha = "2023"))
        elementos.add(Elemento(id = 23, titulo = "Ahora Imagino Cosas", autor = "Él Mató a un Policía Motorizado", fecha = "2023"))
        elementos.add(Elemento(id = 24, titulo = "Mi Próximo Movimiento", autor = "Él Mató a un Policía Motorizado", fecha = "2014"))
        elementos.add(Elemento(id = 25, titulo = "El Magnetismo", autor = "Él Mató a un Policía Motorizado", fecha = "2017"))

        return elementos
    }
}