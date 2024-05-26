package edu.nicolasguerra.meetapp.models.dbModel

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng
import com.google.gson.annotations.SerializedName
import com.google.maps.android.clustering.ClusterItem

@Entity(tableName = "markers")
data class MarkerEntity(
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("latitud")
    val latitud:Double,
    @SerializedName("longitud")
    val longitud:Double,
    @SerializedName("description")
    val description: String?="",
    @SerializedName("titulo")
    val titulo: String?=""
    ) : ClusterItem {



    override fun getPosition(): LatLng {
        return LatLng(latitud,longitud)
    }

    override fun getTitle(): String? {
        return this.titulo
    }

    override fun getSnippet(): String? {
        return this.description
    }

}
