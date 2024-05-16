package edu.nicolasguerra.meetapp.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import edu.nicolasguerra.meetapp.data.MarkerRepository
import edu.nicolasguerra.meetapp.models.Favorito
import edu.nicolasguerra.meetapp.models.dbModel.MarkerEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MainViewModel(private val repository: MarkerRepository) : ViewModel() {

    val allMarkers: Flow<List<MarkerEntity>> = repository.allMarkers

    private val _currentMarkers = repository.fetchMarkers()
    val currentMarkers:Flow<List<MarkerEntity>>
        get() = _currentMarkers

    val allFavoritos: Flow<List<Favorito>> = repository.allFavoritos

    fun insertMarker(markerEntity: MarkerEntity) {
        viewModelScope.launch {
            repository.insertMarker(markerEntity)
        }
    }

    fun deleteMarker(markerEntity: MarkerEntity) {
        viewModelScope.launch {
            repository.deleteMarker(markerEntity)
        }
    }
    fun getMarkerByCoordenadas(coordenadas: String):MarkerEntity {
        return repository.getMarkerByCoordenadas(coordenadas)
    }

    fun insertFavorito(favorito: Favorito) {
        viewModelScope.launch {
            repository.insertFavorito(favorito)
        }
    }

    fun deleteFavorito(favorito: Favorito) {
        viewModelScope.launch {
            repository.deleteFavorito(favorito)
        }
    }
}

class MainViewModelFactory(private val repository: MarkerRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
