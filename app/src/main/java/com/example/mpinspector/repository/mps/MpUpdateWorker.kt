package com.example.mpinspector.repository.mps

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.mpinspector.repository.Repository

/**
 * Background Worker class that fetches latest MPs.
 *
 * @author Niklas Seppälä - 2013018
 * @date 10/10/2021
 */
class MpUpdateWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    companion object {
        const val TAG = "MpUpdateWorker"
    }

    override suspend fun doWork(): Result {
        Repository.mps.loadFromWeb()
        return Result.success()
    }
}