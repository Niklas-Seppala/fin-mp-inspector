package com.example.mpinspector

import android.content.Context

class MyApp {
    companion object {
        lateinit var appContext: Context
        fun initApp(ctx: Context) {
            appContext = ctx
        }
        const val TWITTER_AUTH = "Bearer " + BuildConfig.TWITTER_AUTH
    }
}