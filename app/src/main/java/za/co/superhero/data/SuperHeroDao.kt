package za.co.superhero.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import za.co.superhero.model.SuperHero

@Dao
interface SuperHeroDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert( superhero: SuperHero): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(superHeros: List<SuperHero>): List<Long>

    @Query("SELECT * FROM superhero")
    suspend fun getAllSuperHeros(): List<SuperHero>

    @Query("SELECT * FROM superhero WHERE id = :superHeroId")
    suspend fun getSuperHero(superHeroId: Int): SuperHero

    @Query("DELETE FROM superhero")
    suspend fun deleteAllSuperHeros()
}