package edu.nicolasguerra.meetapp.ui.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import edu.nicolasguerra.meetapp.R
import edu.nicolasguerra.meetapp.databinding.FragmentMapBinding
import edu.nicolasguerra.meetapp.models.dbModel.MarkerEntity
import edu.nicolasguerra.meetapp.ui.detail.MarkerDetalle
import edu.nicolasguerra.meetapp.ui.main.MainViewModel
import kotlinx.coroutines.launch

class MapFragment(private val vm: MainViewModel) : Fragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentMapBinding
    private lateinit var clusterManager: ClusterManager<MarkerEntity>
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var mapView: MapView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapBinding.inflate(inflater, container, false)
        val view = binding.root

        mapView = binding.map
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        swipeRefreshLayout = requireActivity().findViewById(R.id.swipeRefreshLayout) ?: error("SwipeRefreshLayout not found in activity")
        swipeRefreshLayout.setOnRefreshListener {
            cargarMarkers()
        }

        return view
    }

    @SuppressLint("PotentialBehaviorOverride")
    override fun onMapReady(googleMap: GoogleMap) {
        val map = googleMap
        clusterManager = ClusterManager(requireContext(), map)

        map.setOnCameraIdleListener(clusterManager)
        map.setOnMarkerClickListener(clusterManager)

        cargarMarkers()

        map.setOnMapLongClickListener { latLng ->
            val intentDetail = Intent(requireContext(), MarkerDetalle::class.java)
            intentDetail.putExtra("latLang", latLng)
            startActivity(intentDetail)
            map.addMarker(MarkerOptions().position(latLng))
        }

        clusterManager.setOnClusterItemClickListener { item ->
            AlertDialog.Builder(requireContext())
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
