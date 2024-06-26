package edu.nicolasguerra.meetapp.data.apiData

import edu.nicolasguerra.meetapp.models.dbModel.MarkerEntity
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

class Retrofit2Api {
    companion object{
        const val BASE_URL="http://192.168.1.137:8081/"

        fun getRetrofit2Api(): Retrofit2ApiInterface{
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(Retrofit2ApiInterface::class.java)
        }
    }
}
interface Retrofit2ApiInterface {
    @GET("MeetAppMarker")
    suspend fun getApiMarkers(): ArrayList<MarkerEntity>
    @POST("MeetAppMarker")
    suspend fun postApiMarker(@Body marker: MarkerEntity)
    @DELETE("/MeetAppMarker/{latitude}/{longitude}")
    suspend fun deleteApiMarkers(@Path("latitude") latitude: Double, @Path("longitude") longitude: Double)
}
