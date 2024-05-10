package edu.nicolasguerra.meetapp

import android.app.Application
import androidx.room.Room
import edu.nicolasguerra.meetapp.data.dbData.MarkerDatabase

class MyRoomApplication :Application(){
    lateinit var markerDatabase: MarkerDatabase
        private set

    override fun onCreate() {
        super.onCreate()
        markerDatabase= Room.databaseBuilder(
            this,
            MarkerDatabase::class.java,
            name = "markers"
        ).build()
    }
}