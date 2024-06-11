package edu.nicolasguerra.meetapp.data

import android.util.Log
import edu.nicolasguerra.meetapp.data.apiData.Retrofit2Api
import edu.nicolasguerra.meetapp.data.dbData.MarkersDao
import edu.nicolasguerra.meetapp.models.dbModel.MarkerEntity
import edu.nicolasguerra.meetapp.utils.utils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MarkerDataSource(private val markersDao: MarkersDao) {
    private val api= Retrofit2Api.getRetrofit2Api()
    private val utils = utils()

    suspend fun insertMarker(markerEntity: MarkerEntity) {
        markersDao.insertMarker(markerEntity)
        Log.i("marker",markerEntity.toString())
        if (utils.isServerAvailable("http://192.168.1.137:8081/")) {
            api.postApiMarker(markerEntity)
        }
    }


    suspend fun insertMarkerDB(markerEntity: MarkerEntity) {
        markersDao.insertMarker(markerEntity)
        Log.i("marker", markerEntity.toString())
    }
    suspend fun deleteMarker(latidud:Double, longitud:Double) {
        markersDao.deleteMarker(latidud, longitud)
        if (utils.isServerAvailable("http://192.168.1.137:8081/")) {
            api.deleteApiMarkers(latidud, longitud)
        }
    }
    suspend fun deleteMarkerDB(latidud:Double, longitud:Double) {
        markersDao.deleteMarker(latidud, longitud)
    }
    suspend fun updateMarker(latitud: Double, longitud: Double, description: String?) {
        markersDao.updateMarker(latitud, longitud, description)
    }

    fun getApiMarkers()= flow {
        emit(api.getApiMarkers())
    }



    val dbMarkers:Flow<List<MarkerEntity>> = markersDao.getMarkerEntities()


}
