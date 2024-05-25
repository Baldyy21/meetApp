package edu.nicolasguerra.meetapp.ui.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import edu.nicolasguerra.meetapp.MyRoomApplication
import edu.nicolasguerra.meetapp.data.MarkerDataSource
import edu.nicolasguerra.meetapp.data.MarkerRepository
import edu.nicolasguerra.meetapp.databinding.ActivityMainBinding
import edu.nicolasguerra.meetapp.models.dbModel.MarkerEntity
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mapView: MapView
    private lateinit var clusterManager: ClusterManager<MarkerEntity>

    private val vm: MainViewModel by viewModels {
        val db = (application as MyRoomApplication).markerDB
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
        val map = googleMap
        clusterManager = ClusterManager(this, map)

        // Set up cluster manager
        map.setOnCameraIdleListener(clusterManager)
        map.setOnMarkerClickListener(clusterManager)

            lifecycleScope.launch {
                vm.currentMarkers.collect { markers ->
                    clusterManager.clearItems()
                    clusterManager.addItems(markers)
                    clusterManager.cluster()

                }
            }

        map.setOnMapLongClickListener {
            val newMarker = MarkerEntity(coordenadas = it)
            vm.insertMarker(newMarker)
            clusterManager.addItem(newMarker)
            clusterManager.cluster()
        }

        clusterManager.setOnClusterItemClickListener { item ->
            AlertDialog.Builder(this)
                .setMessage("¿Eliminar marcador?")
                .setPositiveButton("Sí") { dialog, which ->
                    vm.deleteMarker(item)
                    clusterManager.removeItem(item)
                    clusterManager.cluster()
                }
                .setNegativeButton("No") { dialog, which -> }
                .show()
            true
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
