package edu.nicolasguerra.meetapp.converters

import androidx.room.TypeConverter
import com.google.android.gms.maps.model.LatLng

class LatLangConverter {
    @TypeConverter
    fun fromLatLng(latLng: LatLng): String {
        return "${latLng.latitude},${latLng.longitude}"
    }


    @TypeConverter
    fun toLatLng(latLngString: String): LatLng {
        var cleanedString = latLngString.replace(Regex("[^0-9.,-]"), "")
        val parts = cleanedString.split(",")
        val latitude = parts[0].toDouble()
        val longitude = parts[1].toDouble()
        return LatLng(latitude, longitude)
    }

}
