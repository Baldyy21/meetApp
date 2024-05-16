package edu.nicolasguerra.meetapp.data

import edu.nicolasguerra.meetapp.converters.LatLangConverter
import edu.nicolasguerra.meetapp.models.Favorito
import edu.nicolasguerra.meetapp.models.apiModel.MeetAppMarker
import edu.nicolasguerra.meetapp.models.dbModel.MarkerEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class MarkerRepository(private val markerDataSource: MarkerDataSource) {
    private val converter = LatLangConverter()
    suspend fun insertMarker(markerEntity: MarkerEntity) {
        markerDataSource.insertMarker(markerEntity)
    }

    suspend fun deleteMarker(markerEntity: MarkerEntity) {
        markerDataSource.deleteMarker(markerEntity)
    }

    val allMarkers: Flow<List<MarkerEntity>> = markerDataSource.dbMarkers

    fun fetchMarkers():Flow<List<MarkerEntity>>{
        return allMarkers
            .combine(markerDataSource.getApiMarkers()) { allMarkersList, apiMarkersList ->
                // Unificar las dos listas eliminando repetidos y convertir a MarkerEntity
                val combinedList = (allMarkersList.map { it } + apiMarkersList.map { it.toMarkerEntity() }).distinctBy { it.id }
                combinedList
            }
    }

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


    fun MeetAppMarker.toMarkerEntity(): MarkerEntity {
        // Aquí debes convertir los campos según sea necesario
        return MarkerEntity(id = this.id, coordenadas = converter.toLatLng(this.latLng), description = this.description)
    }

}
