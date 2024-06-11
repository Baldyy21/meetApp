package edu.nicolasguerra.meetapp.data

import android.util.Log
import edu.nicolasguerra.meetapp.models.dbModel.MarkerEntity
import edu.nicolasguerra.meetapp.utils.utils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class MarkerRepository(private val markerDataSource: MarkerDataSource) {
    private val utils = utils()

    suspend fun insertMarker(markerEntity: MarkerEntity) {
        markerDataSource.insertMarker(markerEntity)
    }
    suspend fun insertMarkerDB(markerEntity: MarkerEntity) {
        markerDataSource.insertMarkerDB(markerEntity)
    }

    suspend fun deleteMarker(latidud:Double, longitud:Double) {
        markerDataSource.deleteMarker(latidud,longitud)
    }
    suspend fun deleteMarkerDB(latidud:Double, longitud:Double) {
        markerDataSource.deleteMarkerDB(latidud,longitud)
    }
    suspend fun updateMarker(latitud: Double, longitud: Double, description: String?) {
    markerDataSource.updateMarker(latitud, longitud, description)
    }

    val allMarkers: Flow<List<MarkerEntity>> = markerDataSource.dbMarkers

    fun fetchMarkers(): Flow<List<MarkerEntity>> = flow {
        val isServerAvailable = utils.isServerAvailable("http://192.168.1.137:8081/")

        if (isServerAvailable) {
            val apiMarkersList = markerDataSource.getApiMarkers().first()
            val dbMarkersList = allMarkers.first()

            val uniqueApiMarkersList = apiMarkersList.filterNot { apiMarker ->
                dbMarkersList.any { it.latitud == apiMarker.latitud && it.longitud == apiMarker.longitud }
            }
            val markersOnlyInDatabase = dbMarkersList.filterNot { dbMarker ->
                apiMarkersList.any { it.latitud == dbMarker.latitud && it.longitud == dbMarker.longitud }
            }

            Log.i("actualizando marcadores", "actualizando marcadores")
            val combinedList = (dbMarkersList + uniqueApiMarkersList).distinctBy { Pair(it.latitud, it.longitud) }

            uniqueApiMarkersList.forEach { insertMarkerDB(it) }

            emit(combinedList)
        } else {
            val dbMarkersList = allMarkers.first()
            emit(dbMarkersList)
        }
    }




}
