package edu.nicolasguerra.meetapp.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import edu.nicolasguerra.meetapp.data.MarkerRepository
import edu.nicolasguerra.meetapp.models.dbModel.MarkerEntity
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: MarkerRepository):ViewModel() {

        fun insertMarker(markerEntity: MarkerEntity) {
            viewModelScope.launch {
                repository.insertMarker(markerEntity)
            }
        }
        fun updateMarker(latitud: Double, longitud: Double, description: String?){
            viewModelScope.launch {
                repository.updateMarker(latitud, longitud, description)
            }
        }



    }

    class DetailViewModelFactory(private val repository: MarkerRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return DetailViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

