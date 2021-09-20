package com.example.mpinspector

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.example.mpinspector.repository.db.MpDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyApp {
    companion object {
        lateinit var appContext: Context
        fun initApp(ctx: Context) {
            appContext = ctx
        }
    }
}

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MyApp.initApp(applicationContext)

        val mpDao = MpDatabase.getInstance().mpDao()
        lifecycleScope.launch(Dispatchers.IO) {
            val users = mpDao.getAll()
        }
    }
}