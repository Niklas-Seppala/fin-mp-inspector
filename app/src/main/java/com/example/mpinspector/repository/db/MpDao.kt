package com.example.mpinspector.repository.db

import androidx.annotation.WorkerThread
import androidx.room.*
import com.example.mpinspector.repository.models.FavoriteModel
import com.example.mpinspector.repository.models.MpModel

@WorkerThread
@Dao
interface MpDao {
    @Query("SELECT * FROM mp")
    suspend fun getAllMps(): List<MpModel>

    @Query("SELECT * FROM favorites ORDER BY timestamp ASC")
    suspend fun getAllFavorites(): List<FavoriteModel>

    @Query("SELECT * FROM mp WHERE personNumber IN (:favIds)")
    suspend fun getAllFavMps(favIds: Array<Int>): List<MpModel>

    @Query("SELECT picture FROM mp WHERE mp.personNumber IS (:id)")
    suspend fun getMpPicById(id: Int): String

    @Query("SELECT * FROM mp WHERE mp.personNumber IS (:id)")
    suspend fun getMpById(id: Int): MpModel

    @Query("SELECT * FROM mp WHERE mp.party IS (:party)")
    suspend fun getMpsByParty(party: String): List<MpModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdate(vararg mps: MpModel)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavorite(mp: FavoriteModel)

    @Delete
    suspend fun deleteFavorite(mp: FavoriteModel)
}
