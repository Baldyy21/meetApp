package edu.nicolasguerra.meetapp.ui.detail

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import edu.nicolasguerra.meetapp.MyRoomApplication
import edu.nicolasguerra.meetapp.R
import edu.nicolasguerra.meetapp.data.MarkerDataSource
import edu.nicolasguerra.meetapp.data.MarkerRepository
import edu.nicolasguerra.meetapp.databinding.ActivityMarkerDetalleBinding
import edu.nicolasguerra.meetapp.models.dbModel.MarkerEntity

class MarkerDetalle : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityMarkerDetalleBinding
    private lateinit var mapView: MapView
    private lateinit var coordenadas : LatLng
    private var isEdicion: Boolean = false

    private val vm: DetailViewModel by viewModels {
        val db = (application as MyRoomApplication).markerDB
        val dataSource= MarkerDataSource(db.markersDao())
        val repository= MarkerRepository(dataSource)
        DetailViewModelFactory(repository)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMarkerDetalleBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        coordenadas = intent.getParcelableExtra("latLang",LatLng::class.java)!!
        if (intent.getStringExtra("description")!=null){
                binding.editText.setText(intent.getStringExtra("description").toString())
            isEdicion = true
        }
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize mapView
        mapView = binding.mapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        binding.btnGuardar.setOnClickListener {
            if (isEdicion){
                updateMarker()
            }else{
                saveMarkerDetails()
            }
        }

    }


    override fun onMapReady(googleMap: GoogleMap) {
        val map = googleMap
        map.uiSettings.setAllGesturesEnabled(false)
        map.mapType = GoogleMap.MAP_TYPE_SATELLITE

        map.addMarker(MarkerOptions().position(coordenadas).draggable(false))

        val cameraPosition = CameraPosition.Builder()
            .target(coordenadas) // Especifica las coordenadas del marcador
            .zoom(15f) // Ajusta el nivel de zoom como desees
            .build()

        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    private fun saveMarkerDetails() {
        val marker = MarkerEntity(longitud = this.coordenadas.longitude, latitud = this.coordenadas.latitude, description = binding.editText.text.toString())
        vm.insertMarker(marker)
        finish()
    }
    private fun updateMarker(){
    vm.updateMarker(longitud = this.coordenadas.longitude, latitud = this.coordenadas.latitude, description = binding.editText.text.toString())
        finish()
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
