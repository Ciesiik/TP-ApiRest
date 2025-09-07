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
        elementos.add(Elemento(1, "Stressed Out", "Twenty One Pilots", "2015", "Himno generacional sobre la ansiedad adulta y la nostalgia por la infancia.", "03:45", 1, "https://www.youtube.com/watch?v=pXRviuL6vMY"))
        elementos.add(Elemento(2, "Ride", "Twenty One Pilots", "2015", "Mezcla reggae-pop con reflexión sobre decisiones y confianza.", "03:34", 1, "https://www.youtube.com/watch?v=Pw-0pbY9JeU"))
        elementos.add(Elemento(3, "Chlorine", "Twenty One Pilots", "2018", "Metáfora sobre el proceso de purificación emocional a través del arte.", "05:24", 2, "https://www.youtube.com/watch?v=Sc8yZRzG4pE"))
        elementos.add(Elemento(4, "Car Radio", "Twenty One Pilots", "2012", "Introspectiva y minimalista, habla del silencio como espacio para pensamientos oscuros.", "04:27", 3, "https://www.youtube.com/watch?v=3sEptl9P0y8"))
        elementos.add(Elemento(5, "Heathens", "Twenty One Pilots", "2016", "Oscura y envolvente, creada para Suicide Squad, explora la exclusión y la desconfianza.", "03:15", 4, "https://www.youtube.com/watch?v=UprcpdwuwCg"))

        elementos.add(Elemento(6, "Do I Wanna Know?", "Arctic Monkeys", "2013", "Sensual y melancólica, sobre el deseo no correspondido.", "04:32", 5, "https://www.youtube.com/watch?v=bpOSxM0rNPM"))
        elementos.add(Elemento(7, "505", "Arctic Monkeys", "2007", "Balada nostálgica sobre volver a un amor perdido.", "04:13", 6, "https://www.youtube.com/watch?v=qU9mHegkTc4"))
        elementos.add(Elemento(8, "I Bet You Look Good on the Dancefloor", "Arctic Monkeys", "2005", "Explosiva y juvenil, retrata la atracción en una pista de baile.", "03:38", 7, "https://www.youtube.com/watch?v=pK7egZaT3hs"))
        elementos.add(Elemento(9, "Why'd You Only Call Me When You're High?", "Arctic Monkeys", "2013", "Crítica a relaciones superficiales y nocturnas.", "02:42", 5, "https://www.youtube.com/watch?v=6366dxFf-Os"))
        elementos.add(Elemento(10, "There’d Better Be a Mirrorball", "Arctic Monkeys", "2022", "Elegante y triste, sobre el fin de una relación con estilo retro.", "04:30", 8, "https://www.youtube.com/watch?v=FY5CAz6S5xs"))

        elementos.add(Elemento(11, "Feel Good Inc.", "Gorillaz", "2005", "Crítica al consumismo y la superficialidad, con base hip-hop y rock alternativo.", "03:41", 9, "https://www.youtube.com/watch?v=HyHNuVaZJ-k"))
        elementos.add(Elemento(12, "Clint Eastwood", "Gorillaz", "2001", "Mezcla de rap y electrónica, sobre el despertar de una nueva conciencia.", "05:41", 10, "https://www.youtube.com/watch?v=H8Qp38qT-xU"))
        elementos.add(Elemento(13, "On Melancholy Hill", "Gorillaz", "2010", "Suave y nostálgica, evoca un lugar de escape emocional.", "03:53", 11, "https://www.youtube.com/watch?v=04mfKJWDSzI"))
        elementos.add(Elemento(14, "Cracker Island", "Gorillaz ft. Thundercat", "2022", "Sobre sectas modernas y alienación, con groove psicodélico.", "03:36", 12, "https://www.youtube.com/watch?v=57Ykv1D0qEE"))
        elementos.add(Elemento(15, "Saturnz Barz", "Gorillaz", "2017", "Experimental y oscuro, con tintes de dancehall y psicodelia.", "04:52", 20, "https://www.youtube.com/watch?v=5nJkZg5qV4s"))

        elementos.add(Elemento(16, "Reptilia", "The Strokes", "2003", "Potente y directo, sobre manipulación emocional.", "03:39", 13, "https://www.youtube.com/watch?v=b8-tXG8KrWs"))
        elementos.add(Elemento(17, "Last Nite", "The Strokes", "2001", "Himno indie sobre desencanto y apatía.", "03:17", 14, "https://www.youtube.com/watch?v=TOypSnKFHrE"))
        elementos.add(Elemento(18, "Someday", "The Strokes", "2001", "Melancólica pero optimista, sobre el paso del tiempo y la esperanza.", "03:07", 14, "https://www.youtube.com/watch?v=knU9gRUWCno"))
        elementos.add(Elemento(19, "Under Cover of Darkness", "The Strokes", "2011", "Regreso enérgico con crítica a la industria musical.", "03:56", 15, "https://www.youtube.com/watch?v=_l09H-3zzgA"))
        elementos.add(Elemento(20, "At The Door", "The Strokes", "2020", "Atmosférica y minimalista, sobre la pérdida y la resignación.", "05:10", 16, "https://www.youtube.com/watch?v=ewOPQZZn4SY"))

        elementos.add(Elemento(21, "El Tesoro", "Él Mató a un Policía Motorizado", "2017", "Melancólica y poética, sobre la búsqueda de sentido en lo cotidiano.", "03:58", 17, "https://www.youtube.com/watch?v=R8ukLdRCy9M"))
        elementos.add(Elemento(22, "La Noche Eterna", "Él Mató a un Policía Motorizado", "2023", "Introspectiva y envolvente, con atmósfera nocturna.", "04:12", 17, "https://www.youtube.com/watch?v=7J1XiSTxdEc"))
        elementos.add(Elemento(23, "Ahora Imagino Cosas", "Él Mató a un Policía Motorizado", "2023", "Mezcla de ternura y desolación, con arreglos sutiles.", "03:46", 17, "https://www.youtube.com/watch?v=JxYVhy7dEuI"))
        elementos.add(Elemento(24, "Mi Próximo Movimiento", "Él Mató a un Policía Motorizado", "2014", "Existencial y directa, sobre decisiones personales.", "04:05", 18, "https://www.youtube.com/watch?v=JFVaBjSGzv4"))
        elementos.add(Elemento(25, "El Magnetismo", "Él Mató a un Policía Motorizado", "2017", "Repetitiva y hipnótica, sobre atracción y destino.", "03:50", 19, "https://www.youtube.com/watch?v=oAjRk-7e7eY"))


        return elementos
    }
}