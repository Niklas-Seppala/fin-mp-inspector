package com.example.mpinspector.repository.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mpinspector.App
import com.example.mpinspector.repository.models.*

/**
 * Database singleton class.
 *
 * @author Niklas Seppälä - 2013018
 * @date 10/10/2021
 */
@Database(version = 1,
    entities = [
        MpModel::class,
        CommentModel::class,
        FavoriteModel::class,
        TwitterFeedModel::class,
        MpTwitterIdModel::class,
        TweetModel::class],
    exportSchema = false)
abstract class MpDatabase : RoomDatabase() {
    abstract fun mpDao(): MpDao
    abstract fun commentDao(): CommentDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun twitterDao(): TwitterFeedDao
    abstract fun mpTwitterDao(): MpTwitterDao
    abstract fun tweetDao(): TweetDao

    companion object {
        @Volatile
        private var mInstance: MpDatabase? = null
        val instance: MpDatabase
            get() {
                synchronized(this) {
                    mInstance = mInstance ?: Room
                        .databaseBuilder(App.appContext, MpDatabase::class.java, "mp-db")
                        .fallbackToDestructiveMigration()
                        .build()
                    return mInstance as MpDatabase

                }
            }
    }
}