package edu.nicolasguerra.meetapp.models.apiModel


import com.google.gson.annotations.SerializedName

data class MeetAppMarker(
    @SerializedName("description")
    val description: String?="",
    @SerializedName("_id")
    val id: String,
    @SerializedName("latLang")
    val latLang: String
)