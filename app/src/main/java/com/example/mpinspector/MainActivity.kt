package com.example.mpinspector

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

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

//        val mpDao = MpDatabase.getInstance().mpDao()
//        lifecycleScope.launch(Dispatchers.IO) {
//            val users = mpDao.getAll()
//        }
    }
}