package edu.nicolasguerra.meetapp.models.dbModel

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import edu.nicolasguerra.meetapp.models.apiModel.MeetAppMarker

@Entity
data class MarkerEntity(
    @PrimaryKey  val id:String,
    val coordenadas:LatLng,
    val description: String?=""
    ) {
    fun toApi(): MeetAppMarker {
    return MeetAppMarker(id=this.id, latLang = this.coordenadas.toString(), description = this.description)
    }
}
