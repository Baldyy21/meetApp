package edu.nicolasguerra.meetapp.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import edu.nicolasguerra.meetapp.models.dbModel.MarkerEntity

@Entity
data class Favorito(
    @PrimaryKey(autoGenerate = true)val idFav:Int=0,
    @Embedded val markerEntity: MarkerEntity,
    val fav: Boolean
)
