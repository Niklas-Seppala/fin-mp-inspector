package com.example.mpinspector

import android.app.Application
import android.content.Context
import androidx.work.*
import com.example.mpinspector.repository.mps.MpUpdateWorker
import java.util.concurrent.TimeUnit

/**
 * Application class that starts background work
 * ands stores application context.
 *
 * @author Niklas Seppälä - 2013018
 * @date 10/10/2021
 */
class App: Application() {
    companion object {
        lateinit var appContext: Context
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext

        val fetchMpsWork = PeriodicWorkRequestBuilder<MpUpdateWorker>(20, TimeUnit.MINUTES)
            .build()
        WorkManager.getInstance(appContext).enqueueUniquePeriodicWork(MpUpdateWorker.TAG,
            ExistingPeriodicWorkPolicy.REPLACE, fetchMpsWork)
    }
}

