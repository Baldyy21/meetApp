package edu.nicolasguerra.meetapp.data

import android.util.Log
import edu.nicolasguerra.meetapp.data.apiData.Retrofit2Api
import edu.nicolasguerra.meetapp.data.dbData.MarkersDao
import edu.nicolasguerra.meetapp.models.Favorito
import edu.nicolasguerra.meetapp.models.dbModel.MarkerEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MarkerDataSource(private val markersDao: MarkersDao) {
    private val api= Retrofit2Api.getRetrofit2Api()


    suspend fun insertMarker(markerEntity: MarkerEntity) {
        markersDao.insertMarker(markerEntity)
        Log.i("marker",markerEntity.toString())

        Log.i("marker",markerEntity.toApi().toString())

        api.postApiMarker(markerEntity.toApi())
    }

    suspend fun postMarkerApi(markerEntity: MarkerEntity) {
        api.postApiMarker(markerEntity.toApi())
        Log.i("marker", markerEntity.toString())
    }
    suspend fun insertMarkerDB(markerEntity: MarkerEntity) {
        markersDao.insertMarker(markerEntity)
        Log.i("marker", markerEntity.toString())
    }
    suspend fun deleteMarker(markerEntity: MarkerEntity) {
        markersDao.deleteMarker(markerEntity)
        api.deleteApiMarkers(markerEntity.id)
    }
    suspend fun getMarkerByCoordenadas(coordenadas: String):MarkerEntity {
            return markersDao.getMarkerByCoordenadas(coordenadas)
    }

    fun getApiMarkers()= flow {
        emit(api.getApiMarkers())
    }
    val dbMarkers:Flow<List<MarkerEntity>> = markersDao.getMarkerEntities()

    suspend fun insertFavorito(favorito: Favorito) {
        markersDao.insertFavorito(favorito)
    }

    suspend fun deleteFavorito(favorito: Favorito) {
        markersDao.deleteFavorito(favorito)
    }

    val allFavoritos:Flow<List<Favorito>> = markersDao.getFavoritos()

}
