package edu.nicolasguerra.meetapp.data

import android.util.Log
import edu.nicolasguerra.meetapp.data.apiData.Retrofit2Api
import edu.nicolasguerra.meetapp.data.dbData.MarkersDao
import edu.nicolasguerra.meetapp.models.dbModel.MarkerEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MarkerDataSource(private val markersDao: MarkersDao) {
    private val api= Retrofit2Api.getRetrofit2Api()


    suspend fun insertMarker(markerEntity: MarkerEntity) {
        markersDao.insertMarker(markerEntity)
        Log.i("marker",markerEntity.toString())

        api.postApiMarker(markerEntity)
    }


    suspend fun insertMarkerDB(markerEntity: MarkerEntity) {
        markersDao.insertMarker(markerEntity)
        Log.i("marker", markerEntity.toString())
    }
    suspend fun deleteMarker(latidud:Double, longitud:Double) {
        api.deleteApiMarkers(latidud, longitud)
        markersDao.deleteMarker(latidud, longitud)
    }
    suspend fun deleteMarkerDB(latidud:Double, longitud:Double) {
        markersDao.deleteMarker(latidud, longitud)
    }

    fun getApiMarkers()= flow {
        emit(api.getApiMarkers())
    }
    val dbMarkers:Flow<List<MarkerEntity>> = markersDao.getMarkerEntities()


}
