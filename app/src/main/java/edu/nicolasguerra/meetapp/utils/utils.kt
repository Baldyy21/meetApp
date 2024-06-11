package edu.nicolasguerra.meetapp.utils

import java.net.HttpURLConnection
import java.net.URL

class utils {
    fun isServerAvailable(url: String): Boolean {
        return try {
            val connection = URL(url).openConnection() as HttpURLConnection
            connection.connectTimeout = 3000 // 3 seconds timeout
            connection.connect()
            connection.responseCode == 200
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}