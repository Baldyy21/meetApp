package edu.nicolasguerra.meetapp.ui.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.MarkerOptions
import edu.nicolasguerra.meetapp.MyRoomApplication
import edu.nicolasguerra.meetapp.data.MarkerDataSource
import edu.nicolasguerra.meetapp.data.MarkerRepository
import edu.nicolasguerra.meetapp.databinding.ActivityMainBinding
import edu.nicolasguerra.meetapp.models.dbModel.MarkerEntity
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mapView: MapView

    private val vm: MainViewModel by viewModels {
        val db = (application as MyRoomApplication).markerDatabase
        val dataSource= MarkerDataSource(db.markersDao())
        val repository= MarkerRepository(dataSource)
        MainViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mapView = binding.map
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        // Configura el mapa aquí
        val map = googleMap
        lifecycleScope.launch {
            vm.allMarkers.collect { markers ->
              markers.forEach {
                  map.addMarker(MarkerOptions().position(it.coordenadas))
              }
            }
        }
        map.setOnMapLongClickListener {
            map.addMarker(MarkerOptions().position(it))
            vm.insertMarker(MarkerEntity(coordenadas = it))
        }
        map.setOnMarkerClickListener { marker ->
            AlertDialog.Builder(this)
                .setMessage("¿Eliminar marcador?")
                .setPositiveButton("Sí") { dialog, which ->
                    marker.remove()
                    vm.deleteMarker(MarkerEntity(coordenadas = marker.position))
                }
                .setNegativeButton("No") { dialog, which ->
                    // No hacer nada si el usuario cancela la eliminación del marcador
                }
                .show()
            true // Devolver true para indicar que el clic en el marcador ha sido manejado
        }
    }


    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
}