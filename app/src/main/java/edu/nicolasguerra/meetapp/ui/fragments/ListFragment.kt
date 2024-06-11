package edu.nicolasguerra.meetapp.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import edu.nicolasguerra.meetapp.R
import edu.nicolasguerra.meetapp.databinding.FragmentListBinding
import edu.nicolasguerra.meetapp.models.dbModel.MarkerEntity
import edu.nicolasguerra.meetapp.ui.detail.MarkerDetalle
import edu.nicolasguerra.meetapp.ui.main.MainViewModel
import kotlinx.coroutines.launch

class ListFragment(private val vm: MainViewModel) : Fragment(), MarkerAdapter.ItemClickListener {

    private lateinit var binding: FragmentListBinding
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = MarkerAdapter(emptyList(), this)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
        swipeRefreshLayout = requireActivity().findViewById(R.id.swipeRefreshLayout) ?: error("SwipeRefreshLayout not found in activity")
        swipeRefreshLayout.setOnRefreshListener {
            cargarMarkersLista(adapter)
        }
        val itemDecoration = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        binding.recyclerView.addItemDecoration(itemDecoration)

        cargarMarkersLista(adapter)
    }

    private fun cargarMarkersLista(adapter: MarkerAdapter) {
        lifecycleScope.launch {
            vm.currentMarkers.collect { markers ->
                adapter.submitList(markers)
            }
        }
        swipeRefreshLayout.isRefreshing = false

    }

    override fun onItemClick(view: View, position: Int) {
        val marker = (binding.recyclerView.adapter as MarkerAdapter).markers[position]
        val intentDetail = Intent(requireContext(), MarkerDetalle::class.java)
        intentDetail.putExtra("latLang", LatLng(marker.latitud,marker.longitud))
        intentDetail.putExtra("description",marker.description)
        startActivity(intentDetail)
    }
}



