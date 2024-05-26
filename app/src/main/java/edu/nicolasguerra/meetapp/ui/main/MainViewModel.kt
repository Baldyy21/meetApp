package edu.nicolasguerra.meetapp.ui.main


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import edu.nicolasguerra.meetapp.data.MarkerRepository
import edu.nicolasguerra.meetapp.models.dbModel.MarkerEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MainViewModel(private val repository: MarkerRepository) : ViewModel() {

    private val _currentMarkers = repository.fetchMarkers()
    val currentMarkers:Flow<List<MarkerEntity>>
        get() = _currentMarkers

    fun deleteMarker(latidud:Double, longitud:Double) {
        viewModelScope.launch {
            repository.deleteMarker(latidud, longitud)
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
