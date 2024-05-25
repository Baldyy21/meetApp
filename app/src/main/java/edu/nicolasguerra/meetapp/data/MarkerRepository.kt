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
    suspend fun insertMarkerDB(markerEntity: MarkerEntity) {
        markerDataSource.insertMarkerDB(markerEntity)
    }
    suspend fun postMarkerApi(markerEntity: MarkerEntity) {
        markerDataSource.postMarkerApi(markerEntity)
    }

    suspend fun deleteMarker(markerEntity: MarkerEntity) {
        markerDataSource.deleteMarker(markerEntity)
    }

    val allMarkers: Flow<List<MarkerEntity>> = markerDataSource.dbMarkers

    // Variable para controlar si la inserción en la base de datos ya ha ocurrido
    private var isInitialInsertionDone = false

    fun fetchMarkers(): Flow<List<MarkerEntity>> {
        val combinedFlow = allMarkers.combine(markerDataSource.getApiMarkers()) { allMarkersList, apiMarkersList ->
            // Convertir los marcadores de la API en entidades MarkerEntity
            val apiMarkersEntityList = apiMarkersList.map { it.toMarkerEntity() }

            // Insertar los marcadores únicos en la base de datos solo la primera vez que se llama a la función
            if (!isInitialInsertionDone) {
                val uniqueApiMarkersList = apiMarkersEntityList.filterNot { apiMarker ->
                    allMarkersList.any { it.id == apiMarker.id }
                }
                val uniqueDbMarkersList = allMarkersList.filterNot { dbMarker ->
                    apiMarkersEntityList.any { it.id == dbMarker.id }
                }
                uniqueApiMarkersList.forEach { insertMarkerDB(it) }
                uniqueDbMarkersList.forEach { postMarkerApi(it) }
                isInitialInsertionDone = true
            }

            // Combinar las listas y eliminar duplicados por ID
            val combinedList = (allMarkersList + apiMarkersEntityList).distinctBy { it.id }

            combinedList
        }

        return combinedFlow
    }





    suspend fun insertFavorito(favorito: Favorito) {
        markerDataSource.insertFavorito(favorito)
    }

    suspend fun deleteFavorito(favorito: Favorito) {
        markerDataSource.deleteFavorito(favorito)
    }
    suspend fun getMarkerByCoordenadas(coordenadas: String):MarkerEntity {
        return markerDataSource.getMarkerByCoordenadas(coordenadas)
    }

    val allFavoritos: Flow<List<Favorito>> = markerDataSource.allFavoritos


    fun MeetAppMarker.toMarkerEntity(): MarkerEntity {
        // Aquí debes convertir los campos según sea necesario
        return MarkerEntity(id = this.id, coordenadas = converter.toLatLng(this.latLng), description = this.description)
    }

}
