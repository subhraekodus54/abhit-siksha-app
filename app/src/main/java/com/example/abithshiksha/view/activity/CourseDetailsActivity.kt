package com.example.abithshiksha.view.activity

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.abithshiksha.R
import com.example.abithshiksha.databinding.ActivityCourseDetailsBinding
import com.example.abithshiksha.helper.PrefManager
import com.example.abithshiksha.model.network.ApiConstants
import com.example.abithshiksha.model.pojo.get_review.Review
import com.example.abithshiksha.model.repo.Outcome
import com.example.abithshiksha.view.adapter.ReviewsAdapter
import com.example.abithshiksha.view_model.GetReviewViewModel
import com.example.abithshiksha.view_model.GetSubjectDetailsViewModel
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.DefaultTimeBar
import com.user.caregiver.gone
import com.user.caregiver.isConnectedToInternet
import com.user.caregiver.lightStatusBar
import com.user.caregiver.visible
import org.koin.androidx.viewmodel.ext.android.viewModel

class CourseDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCourseDetailsBinding
    private var id: Int = 0
    private lateinit var accessToken: String
    private var subject_name: String = ""
    private val mGetSubjectDetailsViewModel: GetSubjectDetailsViewModel by viewModel()
    private val mGetReviewViewModel: GetReviewViewModel by viewModel()

    private lateinit var simpleExoplayer: ExoPlayer
    private var attachmentType: String = ""

    private lateinit var bt_lock: ImageView
    private lateinit var bt_progress_bar: DefaultTimeBar
    private lateinit var bt_fullscreen: ImageView

    private var show_enroll: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCourseDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lightStatusBar(this,R.color.white)

        val extras = intent.extras
        if (extras != null) {
            val subject_id = intent?.getIntExtra("id",0)
            //val subject_id = intent?.getIntExtra("id",0)
            show_enroll = intent?.getBooleanExtra("show_enroll",false)!!
            id = subject_id!!
        }

        //get token
        accessToken = "Bearer "+ PrefManager.getKeyAuthToken()

        binding.subjectDetailsMainLay.gone()
        binding.subjectDetailsShimmerView.startShimmer()

        binding.backArrow.setOnClickListener {
            finish()
        }

        binding.viewDetailsHtv.setOnClickListener {
            val intent = Intent(this, LessonsActivity::class.java)
            intent.putExtra("subject_id",id)
            intent.putExtra("subject_name",subject_name)
            startActivity(intent)
        }

        binding.enrollNowBtn.gone()


        //exo player controll
        bt_fullscreen = findViewById<ImageView>(R.id.bt_fullscreen)
        bt_lock = findViewById<ImageView>(R.id.exo_lock)
        bt_progress_bar = findViewById<DefaultTimeBar>(R.id.exo_progress)

        bt_fullscreen.gone()
        bt_lock.gone()
        bt_progress_bar.gone()

    }

    private fun fillReviewRecyclerView(list: List<Review>) {
        val gridLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.reviewRecycler.apply {
            layoutManager = gridLayoutManager
            adapter = ReviewsAdapter(list,this@CourseDetailsActivity)
        }
    }

    private fun getSubjectDetails(
        token: String,
        subject_id: Int
    ){
        mGetSubjectDetailsViewModel.getSubjectDetails(token,subject_id).observe(this) { outcome ->
            when (outcome) {
                is Outcome.Success -> {
                    if (outcome.data.status == 1) {
                        binding.subjectDetailsMainLay.visible()
                        binding.subjectDetailsShimmerView.stopShimmer()
                        binding.subjectDetailsShimmerView.gone()
                        outcome.data.result.result?.let {
                            if(it.subject_details.total_lesson>1){
                                binding.lessonsCountTv.text = it.subject_details.total_lesson.toString()+" lessons"
                            }else{
                                binding.lessonsCountTv.text = it.subject_details.total_lesson.toString()+" lesson"
                            }

                            if(it.subject_details.total_video>1){
                                binding.cardVideosCountTv.text = it.subject_details.total_video.toString()+" Videos"
                            }else{
                                binding.cardVideosCountTv.text = it.subject_details.total_video.toString()+" Video"
                            }

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                binding.courseDetailsTv.text = Html.fromHtml(it.subject_details.description, Html.FROM_HTML_MODE_LEGACY)
                            }else{
                                binding.courseDetailsTv.text = Html.fromHtml(it.subject_details.description)
                            }
                            binding.boardClassTv.text = it.subject_details.board_name+" > "+"Class "+it.subject_details.class_name

                            if(it.subject_details.total_lesson>1){
                                binding.cardSubjectNameTv.text = it.subject_details.total_lesson.toString()+" Lessons"
                            }else{
                                binding.cardSubjectNameTv.text = it.subject_details.total_lesson.toString()+" Lesson"
                            }

                            it.subject_details.rating?.let {
                                binding.ratingTv.text = it
                            }

                            it.subject_details.why_learn?.let { whylearn ->

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    binding.whyLearnTv.text = Html.fromHtml(whylearn.toString(),Html.FROM_HTML_MODE_LEGACY)
                                }else{
                                    binding.whyLearnTv.text = Html.fromHtml(whylearn.toString())
                                }

                            }

                            binding.subjectNameTv.text = it.subject_details.subject_name
                            subject_name = it.subject_details.subject_name.toString()
                            binding.cardLessonsCountTv.text = it.subject_details.subject_name.toString()
                            binding.subjectNameTv.text = it.subject_details.subject_name.toString()

                            //attachment
                            if(it.subject_attachment.attachment_type == "video"){

                                attachmentType = "video"
                                //val uri: Uri = Uri.parse(ApiConstants.PUBLIC_URL+it.subject_attachment.attachment)
                                val uri: String = ApiConstants.PUBLIC_URL+it.subject_attachment.attachment

                                exoplayer(uri)

                            }else{

                                attachmentType = "image"

                                binding.player.gone()
                                binding.courseDetailsImg.visible()
                                Glide.with(this)
                                    .load(ApiConstants.PUBLIC_URL+it.subject_attachment.attachment) // image url
                                    .placeholder(R.drawable.error_img_bg) // any placeholder to load at start
                                    .centerCrop()
                                    .into(binding.courseDetailsImg)
                            }

                            if(it.subject_details.already_purchase == 0 && it.subject_details.already_incart == 0){
                                if(show_enroll == true){
                                    binding.enrollNowBtn.visible()
                                    binding.enrollNowBtn.setOnClickListener {click ->
                                        val intent = Intent(this, EnrollNowActivity::class.java)
                                        intent.putExtra("board",it.subject_details.board_name)
                                        intent.putExtra("class",it.subject_details.class_name)
                                        intent.putExtra("subject_id",it.subject_details.id)
                                        startActivity(intent)
                                    }
                                }else{
                                    binding.enrollNowBtn.gone()
                                }
                            }else{
                                binding.enrollNowBtn.gone()
                            }
                        }

                    } else {
                        Toast.makeText(this, outcome.data.result.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                is Outcome.Failure<*> -> {
                    Toast.makeText(this, R.string.network_issue, Toast.LENGTH_SHORT).show()
                    Log.i("statusMsg", outcome.e.message.toString())

                    outcome.e.printStackTrace()
                    Log.i("status", outcome.e.cause.toString())
                }
            }
        }
    }

    private fun getReview(
        token: String,
        subject_id: Int
    ){
        binding.reviewRecycler.gone()
        binding.reviewShimmerView.visible()
        binding.reviewShimmerView.startShimmer()
        binding.noReviewTv.gone()
        mGetReviewViewModel.getReview(token,subject_id).observe(this) { outcome ->
            when (outcome) {
                is Outcome.Success -> {
                    if (outcome.data.status == 1) {
                        /*Toast.makeText(this, outcome.data.result.message, Toast.LENGTH_SHORT)
                            .show()*/
                        binding.reviewShimmerView.gone()
                        binding.reviewShimmerView.stopShimmer()

                        if(outcome.data.result.reviews != null && outcome.data.result.reviews.isNotEmpty()){
                            binding.reviewRecycler.visible()
                            fillReviewRecyclerView(outcome.data.result.reviews)
                            binding.noReviewTv.gone()
                        }else{
                            binding.noReviewTv.visible()
                            binding.reviewRecycler.gone()
                        }

                    } else {
                        Toast.makeText(this, outcome.data.result.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                is Outcome.Failure<*> -> {
                    Toast.makeText(this, R.string.network_issue, Toast.LENGTH_SHORT).show()

                    Log.i("statusMsg", outcome.e.message.toString())

                    outcome.e.printStackTrace()
                    Log.i("status", outcome.e.cause.toString())
                }
            }
        }
    }

    private fun exoplayer(uri: String){
        binding.player.visible()
        binding.courseDetailsImg.gone()

        simpleExoplayer = ExoPlayer.Builder(this)
            .setSeekBackIncrementMs(5000)
            .setSeekForwardIncrementMs(5000)
            .build()
        binding.player.player = simpleExoplayer
        //binding.player.keepScreenOn = true

        simpleExoplayer.addListener(object: Player.Listener{
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                if(playbackState == Player.STATE_BUFFERING){
                    binding.progressBar.visibility = View.VISIBLE
                }else if(playbackState == Player.STATE_READY){
                    binding.progressBar.visibility = View.GONE
                }
            }
        })

        val mediaItem = MediaItem.fromUri(uri)
        simpleExoplayer.setMediaItem(mediaItem)
        simpleExoplayer.prepare()
        //simpleExoplayer.play()
        simpleExoplayer.pause()
    }

    override fun onStart() {

        if(isConnectedToInternet()){
            getSubjectDetails(accessToken,id)
            getReview(accessToken,id)
        }else{
            Toast.makeText(this,"No internet connection.",Toast.LENGTH_LONG).show()
        }

        super.onStart()
    }

    override fun onPause() {
        if(attachmentType == "video"){
            simpleExoplayer.pause()
        }
        super.onPause()
    }

    override fun onStop() {
        if(attachmentType == "video"){
            simpleExoplayer.stop()
        }
        super.onStop()
    }

    override fun onDestroy() {
        if(attachmentType == "video"){
            simpleExoplayer.release()
        }
        super.onDestroy()
    }
}