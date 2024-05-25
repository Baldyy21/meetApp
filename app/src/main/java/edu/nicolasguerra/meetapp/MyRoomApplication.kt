package edu.nicolasguerra.meetapp

import android.app.Application
import androidx.room.Room
import edu.nicolasguerra.meetapp.data.dbData.MarkerDB

class MyRoomApplication :Application(){
    lateinit var markerDB: MarkerDB
        private set

    override fun onCreate() {
        super.onCreate()
        markerDB= Room.databaseBuilder(
            this,
            MarkerDB::class.java,
            name = "MeetAppMarkers"
        ).build()
    }
}