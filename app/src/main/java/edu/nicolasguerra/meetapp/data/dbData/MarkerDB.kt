package edu.nicolasguerra.meetapp.data.dbData

import androidx.room.Dao
import androidx.room.Database
import androidx.room.DatabaseConfiguration
import androidx.room.RoomDatabase
import androidx.room.Insert
import androidx.room.Delete
import androidx.room.InvalidationTracker
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteOpenHelper
import edu.nicolasguerra.meetapp.converters.LatLangConverter
import edu.nicolasguerra.meetapp.models.Favorito
import edu.nicolasguerra.meetapp.models.dbModel.MarkerEntity
import kotlinx.coroutines.flow.Flow

@Database(entities = [MarkerEntity::class, Favorito::class], version = 1)
@TypeConverters(LatLangConverter::class)
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
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMarker(markerEntity: MarkerEntity)

    @Delete
    suspend fun deleteMarker(markerEntity: MarkerEntity)

    @Query("SELECT * FROM MarkerEntity")
    fun getMarkerEntities(): Flow<List<MarkerEntity>>

    @Query("SELECT * FROM MarkerEntity WHERE coordenadas = :coordenadas")
    fun getMarkerByCoordenadas(coordenadas: String): MarkerEntity


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorito(favorito: Favorito)

    @Delete
    suspend fun deleteFavorito(favorito: Favorito)

    @Query("SELECT * FROM Favorito")
    fun getFavoritos(): Flow<List<Favorito>>
}
