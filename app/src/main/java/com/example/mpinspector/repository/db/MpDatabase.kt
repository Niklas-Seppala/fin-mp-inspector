package com.example.mpinspector.repository.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mpinspector.MyApp
import com.example.mpinspector.repository.models.CommentModel
import com.example.mpinspector.repository.models.MemberOfParliamentModel

@Database(version = 1, entities = [MemberOfParliamentModel::class, CommentModel::class],
          exportSchema = false)
abstract class MpDatabase : RoomDatabase() {
    abstract fun mpDao(): MpDao

    companion object {
        @Volatile
        private var INSTANCE: MpDatabase? = null
        fun getInstance(): MpDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if(instance == null) {
                    instance = Room.databaseBuilder(MyApp.appContext,
                                                    MpDatabase::class.java, "mp-db")
                        .fallbackToDestructiveMigration().build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}