package edu.nicolasguerra.meetapp.models.dbModel

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng
import edu.nicolasguerra.meetapp.models.apiModel.MeetAppMarker

@Entity
data class MarkerEntity(
    @PrimaryKey(autoGenerate = true)val id:Int = 0,
    val coordenadas:LatLng,
    val description: String?=""
    ) {
    fun toApi(): MeetAppMarker {
    return MeetAppMarker(description = this.description, latLang = this.coordenadas.toString(),id=this.id )
    }
}
