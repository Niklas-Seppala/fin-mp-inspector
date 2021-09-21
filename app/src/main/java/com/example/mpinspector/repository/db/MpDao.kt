package com.example.mpinspector.repository.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.mpinspector.repository.models.MemberOfParliamentModel

@Dao
interface MpDao {
    @Query("SELECT * FROM mp")
    fun getAll(): LiveData<List<MemberOfParliamentModel>>

    @Query("SELECT * FROM mp WHERE mp.id IS (:id)")
    fun queryById(id: Int): LiveData<MemberOfParliamentModel>

    @Query("SELECT * FROM mp WHERE mp.party IS (:party)")
    fun queryByParty(party: String): LiveData<List<MemberOfParliamentModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdate(vararg mps: MemberOfParliamentModel)

    @Delete
    fun delete(user: MemberOfParliamentModel)
}