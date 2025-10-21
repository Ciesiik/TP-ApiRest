package com.example.tp_apirest.fragmentos

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.example.tp_apirest.R
import com.example.tp_apirest.activities.album.AlbumsActivity
import com.example.tp_apirest.activities.artist.ArtistsActivity
import com.example.tp_apirest.activities.track.TracksActivity

class PrimerFragmento: Fragment() {
    var listener: PrimerFragmentoInterfaz? = null

    lateinit var btnTracks: ImageButton
    private lateinit var btnAlbums: ImageButton
    private lateinit var btnArtists: ImageButton
    lateinit var btnBuscar: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.primer_fragmento, container, false)
        btnTracks = view.findViewById(R.id.btnTracks)
        btnAlbums = view.findViewById(R.id.btnAlbums)
        btnArtists = view.findViewById(R.id.btnArtists)
        btnBuscar = view.findViewById(R.id.btnBuscar)

        btnTracks.setOnClickListener {
            val intent = Intent(requireContext(), TracksActivity::class.java)
            startActivity(intent)
        }
        btnAlbums.setOnClickListener {
            val intent = Intent(requireContext(), AlbumsActivity::class.java)
            startActivity(intent)
        }
        btnArtists.setOnClickListener {
            val intent = Intent(requireContext(), ArtistsActivity::class.java)
            startActivity(intent)
        }
        btnBuscar.setOnClickListener {
            listener?.mostrarBusqueda()
        }


        return view
    }
}