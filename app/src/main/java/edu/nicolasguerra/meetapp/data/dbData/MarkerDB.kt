package edu.nicolasguerra.meetapp.data.dbData

import androidx.room.Dao
import androidx.room.Database
import androidx.room.DatabaseConfiguration
import androidx.room.RoomDatabase
import androidx.room.Insert
import androidx.room.InvalidationTracker
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.sqlite.db.SupportSQLiteOpenHelper
import edu.nicolasguerra.meetapp.models.Favorito
import edu.nicolasguerra.meetapp.models.dbModel.MarkerEntity
import kotlinx.coroutines.flow.Flow

@Database(entities = [MarkerEntity::class, Favorito::class], version = 1)
abstract class MarkerDB : RoomDatabase() {
    abstract fun markersDao(): MarkersDao

    override fun clearAllTables() {
        // Implementa la lógica para borrar todas las tablas
    }

    override fun createInvalidationTracker(): InvalidationTracker {
        // Implementa la lógica para crear el InvalidationTracker
        TODO("Not yet implemented")
    }

    override fun createOpenHelper(config: DatabaseConfiguration): SupportSQLiteOpenHelper {
        // Implementa la lógica para crear el OpenHelper
        TODO("Not yet implemented")
    }
}

@Dao
interface MarkersDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMarker(markerEntity: MarkerEntity)

    @Query("DELETE FROM markers WHERE latitud = :latitud AND longitud = :longitud")
    suspend fun deleteMarker(latitud: Double, longitud: Double)

    @Query("SELECT * FROM markers")
    fun getMarkerEntities(): Flow<List<MarkerEntity>>

    @Query("UPDATE markers SET description = :description WHERE latitud = :latitud AND longitud = :longitud")
    suspend fun updateMarker(latitud: Double, longitud: Double, description: String?)
}
