package edu.nicolasguerra.meetapp.models.apiModel


import com.google.gson.annotations.SerializedName

data class MeetAppMarker(
    @SerializedName("description")
    val description: String?="",
    @SerializedName("id")
    val id: Int,
    @SerializedName("latLng")
    val latLng: String
)