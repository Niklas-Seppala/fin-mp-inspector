package com.example.mpinspector.repository.db

import androidx.room.*
import com.example.mpinspector.repository.models.MemberOfParliamentModel

@Dao
interface MpDao {
    @Query("SELECT * FROM mp")
    suspend fun getAll(): List<MemberOfParliamentModel>

    @Query("SELECT picture FROM mp WHERE mp.personNumber IS (:id)")
    suspend fun queryPicEndpoint(id: Int): String

    @Query("SELECT * FROM mp WHERE mp.personNumber IS (:id)")
    suspend fun queryById(id: Int): MemberOfParliamentModel

    @Query("SELECT * FROM mp WHERE mp.party IS (:party)")
    suspend fun queryByParty(party: String): List<MemberOfParliamentModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdate(vararg mps: MemberOfParliamentModel)
}