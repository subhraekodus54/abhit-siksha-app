package com.example.abithshiksha.view.activity

import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.abithshiksha.R
import com.example.abithshiksha.databinding.ActivityVideoPlayBinding
import com.example.abithshiksha.helper.PrefManager
import com.example.abithshiksha.model.network.ApiConstants
import com.example.abithshiksha.model.repo.Outcome
import com.example.abithshiksha.service.VideoWatchTimeService
import com.example.abithshiksha.view_model.SubmitWatchTimeViewModel
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.DefaultTimeBar
import com.user.caregiver.gone
import com.user.caregiver.lightStatusBar
import com.user.caregiver.visible
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*


class VideoPlayActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVideoPlayBinding
    private lateinit var accessToken: String

    companion object{
        private var isFullScreen = false
        private var isLock = false
    }
    private lateinit var simpleExoplayer: ExoPlayer
    private lateinit var bt_lock: ImageView
    private lateinit var bt_progress_bar: DefaultTimeBar
    private lateinit var bt_fullscreen: ImageView
    private lateinit var video_url: String

    private var video_id: Int = 0
    private var startTime: String = ""
    private var user_id: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoPlayBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lightStatusBar(this,R.color.white)

        val extras = intent.extras
        if (extras != null) {
            video_url = intent?.getStringExtra("url").toString()
            video_id = intent?.getIntExtra("id",0)!!
            user_id = intent?.getStringExtra("user_id").toString()
        }
        startTime = startTime()

        //get token
        accessToken = "Bearer "+ PrefManager.getKeyAuthToken()

        simpleExoplayer = ExoPlayer.Builder(this)
            .setSeekBackIncrementMs(5000)
            .setSeekForwardIncrementMs(5000)
            .build()
        binding.player.player = simpleExoplayer
        binding.player.keepScreenOn = true

        bt_fullscreen = findViewById<ImageView>(R.id.bt_fullscreen)
        bt_lock = findViewById<ImageView>(R.id.exo_lock)
        bt_progress_bar = findViewById<DefaultTimeBar>(R.id.exo_progress)

        bt_fullscreen.setOnClickListener {
            if(!isFullScreen){
                bt_fullscreen.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.ic_baseline_fullscreen_exit))
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE)
            }else{
                bt_fullscreen.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.ic_baseline_fullscreen))
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
            }
            isFullScreen = !isFullScreen
        }

        bt_lock.setOnClickListener {
            if(!isLock){
                bt_lock.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.ic_baseline_lock))
                bt_progress_bar.gone()
            }else{
                bt_lock.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.ic_baseline_lock_open))
                bt_progress_bar.visible()
            }
            isLock = !isLock
            lockScreen(isLock)
        }

        simpleExoplayer.addListener(object: Player.Listener{
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                if(playbackState == Player.STATE_BUFFERING){
                    binding.progressBar.visibility = View.VISIBLE
                }else if(playbackState == Player.STATE_READY){
                    binding.progressBar.visibility = View.GONE
                }
            }
        })

        val mediaItem = MediaItem.fromUri(ApiConstants.PUBLIC_URL+video_url)
        simpleExoplayer.setMediaItem(mediaItem)
        simpleExoplayer.prepare()
        simpleExoplayer.play()

        user_id?.let {
            binding.userIdTv.text = user_id
        }
    }

    private fun lockScreen(lock: Boolean) {
        val sec_vid = findViewById<LinearLayout>(R.id.sec_controllvid1)
        val sec_button = findViewById<LinearLayout>(R.id.sec_controllvid2)
        if(lock){
            sec_vid.visibility = View.INVISIBLE
            sec_button.visibility = View.INVISIBLE
        }else{
            sec_vid.visibility = View.VISIBLE
            sec_button.visibility = View.VISIBLE
        }
    }

    override fun onBackPressed() {
        if(isLock) return
        if(resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
            bt_fullscreen.performClick()
        }
        else super.onBackPressed()
    }

    override fun onStop() {
        //service
        Intent(this, VideoWatchTimeService::class.java).also {
            it.putExtra("id",video_id)
            it.putExtra("start_time",startTime)
            it.putExtra("end_time",endTime())
            startService(it)
        }
        simpleExoplayer.stop()
        super.onStop()
    }

    override fun onDestroy() {
        Log.e("current",(simpleExoplayer.getCurrentPosition()/1000).toString())
        Log.e("current","end time: "+endTime())

        simpleExoplayer.release()
        super.onDestroy()
    }

    override fun onPause() {
        simpleExoplayer.pause()
        super.onPause()
    }

    private fun startTime():String{
        val simpleDateFormate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val startTime = simpleDateFormate.format(Date())
        return startTime
    }

    private fun endTime():String{
        val simpleDateFormate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val endTime = simpleDateFormate.format(Date())
        return endTime
    }

}