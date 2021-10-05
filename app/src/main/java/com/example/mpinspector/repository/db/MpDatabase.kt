package com.example.mpinspector.repository.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mpinspector.MyApp
import com.example.mpinspector.repository.models.*

@Database(version = 1,
    entities = [MpModel::class, CommentModel::class, FavoriteModel::class,
        TwitterFeedModel::class, MpTwitterIdModel::class, ReadTweet::class],
    exportSchema = false)
abstract class MpDatabase : RoomDatabase() {
    abstract fun mpDao(): MpDao
    abstract fun commentDao(): CommentDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun twitterDao(): TwitterDao
    abstract fun mpTwitterDao(): MpTwitterDao
    abstract fun ReadTweetDao(): ReadTweetDao

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