package com.example.mpinspector.repository.db

import androidx.room.*

/**
 *  Generic base DAO class for basic database
 *  operations.
 *
 * @author Niklas Seppälä - 2013018
 * @date 10/10/2021
 */
@Dao
abstract class GenericDao<T> {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insert(item: T): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insert(item: List<T>): List<Long>

    @Update
    abstract fun update(item: T)

    @Update
    abstract fun update(item: List<T>)

    @Delete
    abstract fun delete(item: T)

    @Transaction
    open fun insertOrUpdate(item: T) {
        val id = insert(item)
        if (id == -1L) update(item)
    }

    @Transaction
    open fun insertOrUpdate(items: List<T>) {
        val insertResult = insert(items)
        val updateList = mutableListOf<T>()

        for (i in insertResult.indices) {
            if (insertResult[i] == -1L) updateList.add(items[i])
        }

        if (updateList.isNotEmpty()) update(updateList)
    }
}