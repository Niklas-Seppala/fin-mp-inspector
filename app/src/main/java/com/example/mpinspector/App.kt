package com.example.mpinspector

import android.content.Context

/**
 * @author Niklas Seppälä - 2013018
 * @date 10/10/2021
 */
class App {
    companion object {
        lateinit var appContext: Context
        fun initApp(ctx: Context) {
            appContext = ctx
        }
        const val TWITTER_AUTH = "Bearer " + BuildConfig.TWITTER_AUTH
    }
}