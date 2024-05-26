package edu.nicolasguerra.meetapp.ui.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.maps.android.clustering.ClusterManager
import edu.nicolasguerra.meetapp.MyRoomApplication
import edu.nicolasguerra.meetapp.data.MarkerDataSource
import edu.nicolasguerra.meetapp.data.MarkerRepository
import edu.nicolasguerra.meetapp.databinding.ActivityMainBinding
import edu.nicolasguerra.meetapp.models.dbModel.MarkerEntity
import edu.nicolasguerra.meetapp.ui.detail.MarkerDetalle
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mapView: MapView
    private lateinit var clusterManager: ClusterManager<MarkerEntity>
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout


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
        swipeRefreshLayout=binding.swipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener {
            cargarMarkers()
        }
    }

    @SuppressLint("PotentialBehaviorOverride")
    override fun onMapReady(googleMap: GoogleMap) {
        val map = googleMap
        clusterManager = ClusterManager(this, map)

        // Set up cluster manager
        map.setOnCameraIdleListener(clusterManager)
        map.setOnMarkerClickListener(clusterManager)

        cargarMarkers()

        map.setOnMapLongClickListener { latLng ->
            val intentDetail = Intent(this, MarkerDetalle::class.java)
            intentDetail.putExtra("latLang", latLng)
            startActivity(intentDetail)
        }

        clusterManager.setOnClusterItemClickListener { item ->
            AlertDialog.Builder(this)
                .setMessage("¿Eliminar marcador?")
                .setPositiveButton("Sí") { _, _ ->
                    vm.deleteMarker(latidud = item.latitud, longitud = item.longitud)
                    clusterManager.removeItem(item)
                    clusterManager.cluster()
                }
                .setNegativeButton("No") { _, _ -> }
                .show()
            true
        }
    }

    private fun cargarMarkers() {
        lifecycleScope.launch {
            vm.currentMarkers.collect { markers ->
                clusterManager.clearItems()
                clusterManager.addItems(markers)
                clusterManager.cluster()
            }
        }
        swipeRefreshLayout.isRefreshing = false
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
