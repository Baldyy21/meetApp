package edu.nicolasguerra.meetapp.data.apiData

import edu.nicolasguerra.meetapp.models.apiModel.MeetAppMarker
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

class Retrofit2Api {
    companion object{
        const val BASE_URL="localhost:8082/"

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
    @GET("meetAppMarkers")
    suspend fun getApiMarkers(): ArrayList<MeetAppMarker>
    @POST("meetAppMarker")
    suspend fun postApiMarker(marker: MeetAppMarker)
    @DELETE("meetAppMarkers/{id}")
    suspend fun deleteApiMarkers(@Path("id")id:String)
}
