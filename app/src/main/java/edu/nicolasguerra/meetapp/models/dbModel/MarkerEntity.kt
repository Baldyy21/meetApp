package edu.nicolasguerra.meetapp.models.dbModel

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng
import edu.nicolasguerra.meetapp.models.apiModel.MeetAppMarker
import edu.nicolasguerra.meetapp.converters.LatLangConverter

@Entity
data class MarkerEntity(
    @PrimaryKey(autoGenerate = true)val id:Int = 0,
    val coordenadas:LatLng,
    val description: String?=""
    ) {
    fun toApi(): MeetAppMarker {
    return MeetAppMarker(description = this.description, latLng = this.coordenadas.toString(),id=this.id )
    }
}
