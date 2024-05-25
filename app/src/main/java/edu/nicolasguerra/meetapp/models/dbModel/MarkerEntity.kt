package edu.nicolasguerra.meetapp.models.dbModel

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import edu.nicolasguerra.meetapp.models.apiModel.MeetAppMarker
import edu.nicolasguerra.meetapp.converters.LatLangConverter

@Entity(tableName = "markers")
data class MarkerEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val coordenadas:LatLng,
    val description: String?=""
    ) : ClusterItem {
    fun toApi(): MeetAppMarker {
    return MeetAppMarker(description = this.description, latLng = this.coordenadas.toString(),id=this.id )
    }

    override fun getPosition(): LatLng {
        return this.coordenadas
    }

    override fun getTitle(): String? {
        return this.description
    }

    override fun getSnippet(): String? {
        return this.description
    }
}
