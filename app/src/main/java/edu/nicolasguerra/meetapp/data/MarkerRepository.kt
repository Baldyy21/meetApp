package edu.nicolasguerra.meetapp.data

import android.util.Log
import edu.nicolasguerra.meetapp.models.dbModel.MarkerEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class MarkerRepository(private val markerDataSource: MarkerDataSource) {
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

    val allMarkers: Flow<List<MarkerEntity>> = markerDataSource.dbMarkers

    var initialInsertion:Boolean=false
    fun fetchMarkers(): Flow<List<MarkerEntity>> {
        val combinedFlow = allMarkers.combine(markerDataSource.getApiMarkers()) { dbMarkersList, apiMarkersList ->

                val uniqueApiMarkersList = apiMarkersList.filterNot { apiMarker ->
                    dbMarkersList.any { it.latitud == apiMarker.latitud && it.longitud == apiMarker.longitud }
                }
                val markersOnlyInDatabase = dbMarkersList.filterNot { dbMarker ->
                apiMarkersList.any { it.latitud == dbMarker.latitud && it.longitud == dbMarker.longitud }
                }


                if (!initialInsertion) {
                    uniqueApiMarkersList.forEach { insertMarkerDB(it) }
                    markersOnlyInDatabase.forEach { deleteMarkerDB(it.latitud,it.longitud) }
                    initialInsertion=true
                }
            Log.i("actualizando marcadores","actualizando marcadores")
            val combinedList = (dbMarkersList + apiMarkersList).distinctBy { Pair(it.latitud, it.longitud) }

            combinedList
        }

        return combinedFlow
    }



}
