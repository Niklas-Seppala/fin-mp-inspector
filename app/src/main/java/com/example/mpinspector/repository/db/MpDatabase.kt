package com.example.mpinspector.repository.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mpinspector.MyApp
import com.example.mpinspector.repository.models.CommentModel
import com.example.mpinspector.repository.models.FavoriteModel
import com.example.mpinspector.repository.models.MpModel

@Database(version = 1, entities = [MpModel::class, CommentModel::class, FavoriteModel::class],
          exportSchema = false)
abstract class MpDatabase : RoomDatabase() {
    abstract fun mpDao(): MpDao
    abstract fun commentDao(): CommentDao

    companion object {
        @Volatile
        private var mInstance: MpDatabase? = null
        val instance: MpDatabase
            get() {
                synchronized(this) {
                    mInstance = mInstance ?: Room
                        .databaseBuilder(MyApp.appContext, MpDatabase::class.java, "mp-db")
                        .fallbackToDestructiveMigration()
                        .build()
                    return mInstance as MpDatabase

                }
            }
    }
}