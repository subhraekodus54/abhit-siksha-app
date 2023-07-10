package com.example.abithshiksha.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ServiceLifecycleDispatcher
import com.example.abithshiksha.helper.PrefManager
import com.example.abithshiksha.model.network.ApiClient
import com.example.abithshiksha.model.pojo.watch_time.SubmitWatchTimeRequest
import com.example.abithshiksha.model.pojo.watch_time.SubmitWatchTimeResponse
import com.example.abithshiksha.model.repo.Outcome
import com.example.abithshiksha.model.repo.submit_watch_time_repo.SubmitWatchTimeRepository
import com.example.abithshiksha.view_model.SubmitWatchTimeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class VideoWatchTimeService(): Service() {
    private val apiService = ApiClient.getInstance(this)
    private lateinit var accessToken: String
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    val TAG = "MyService"

    init {
        Log.d(TAG,"Service is running...")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val id = intent?.getIntExtra("id",0)
        val start_time = intent?.getStringExtra("start_time")
        val end_time = intent?.getStringExtra("end_time")

        //get token
        accessToken = "Bearer "+ PrefManager.getKeyAuthToken()

        scope.launch {
            submitWatchTime(id!!, start_time.toString(), end_time.toString(),accessToken)
        }

        return START_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    suspend fun submitWatchTime(
        video_id: Int,
        video_start_time: String,
        video_end_time: String,
        token: String
    ){
        val apiResponse = MutableLiveData<Outcome<SubmitWatchTimeResponse>>()
        try {
            val response = apiService.submitWatchTime(SubmitWatchTimeRequest(video_id,video_start_time,video_end_time),token)
            //apiResponse.value = Outcome.success(response!!)
            Log.d(TAG,"response: "+response)
            stopSelf()
        } catch (e: Throwable) {
            //apiResponse.value = Outcome.failure(e)
            Log.d(TAG,"msg: "+e)
        }
    }
}