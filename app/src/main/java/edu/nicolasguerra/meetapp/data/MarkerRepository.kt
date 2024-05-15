package edu.nicolasguerra.meetapp.data

import edu.nicolasguerra.meetapp.models.Favorito
import edu.nicolasguerra.meetapp.models.dbModel.MarkerEntity
import kotlinx.coroutines.flow.Flow

class MarkerRepository(private val markerDataSource: MarkerDataSource) {

    suspend fun insertMarker(markerEntity: MarkerEntity) {
        markerDataSource.insertMarker(markerEntity)
    }

    suspend fun deleteMarker(markerEntity: MarkerEntity) {
        markerDataSource.deleteMarker(markerEntity)
    }

    val allMarkers: Flow<List<MarkerEntity>> = markerDataSource.dbMarkers

    suspend fun insertFavorito(favorito: Favorito) {
        markerDataSource.insertFavorito(favorito)
    }

    suspend fun deleteFavorito(favorito: Favorito) {
        markerDataSource.deleteFavorito(favorito)
    }
    fun getMarkerByCoordenadas(coordenadas: String):MarkerEntity {
        return markerDataSource.getMarkerByCoordenadas(coordenadas)
    }

    val allFavoritos: Flow<List<Favorito>> = markerDataSource.allFavoritos

}
