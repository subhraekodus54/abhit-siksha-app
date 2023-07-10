package com.example.abithshiksha.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.abithshiksha.R
import com.example.abithshiksha.databinding.ActivityAddReviewBinding
import com.example.abithshiksha.databinding.ActivityNotificationBinding
import com.example.abithshiksha.helper.PrefManager
import com.example.abithshiksha.model.network.ApiConstants
import com.example.abithshiksha.model.repo.Outcome
import com.example.abithshiksha.view_model.AddReviewViewModel
import com.example.abithshiksha.view_model.ChangePasswordViewModel
import com.user.caregiver.gone
import com.user.caregiver.isConnectedToInternet
import com.user.caregiver.lightStatusBar
import com.user.caregiver.visible
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddReviewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddReviewBinding
    private val mAddReviewViewModel: AddReviewViewModel by viewModel()
    private lateinit var accessToken: String
    private var id: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lightStatusBar(this,R.color.white)
        binding.progressBar.gone()

        val extras = intent.extras
        if (extras != null) {
            val cart_id = intent?.getIntExtra("id",0)
            id = cart_id!!
            val image = intent?.getStringExtra("image")
            val name = intent?.getStringExtra("name")

            Glide.with(this)
                .load(ApiConstants.PUBLIC_URL+image) // image url
                .placeholder(R.drawable.error_img_bg) // any placeholder to load at start
                .centerCrop()
                .into(binding.courseImg)
            binding.courseName.text = name
        }
        //get token
        accessToken = "Bearer "+ PrefManager.getKeyAuthToken()

        binding.backArrow.setOnClickListener {
            finish()
        }

        binding.submitLay.setOnClickListener {
            val review = binding.reviewEdTxt.text.toString()
            val rating = binding.courseRatingBar.rating
            if(review.isNotEmpty()){
                if(rating.toString().isNotEmpty()){
                    if(isConnectedToInternet()){
                        addReview(id,review,rating.toString(),accessToken)
                    }else{
                        Toast.makeText(this, "No internet connection.", Toast.LENGTH_SHORT)
                            .show()
                    }
                }else{
                    Toast.makeText(this, "Please provide your rating.", Toast.LENGTH_SHORT)
                        .show()
                }
            }else{
                Toast.makeText(this, "Please provide your review.", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun addReview(
        subject_id: Int,
        review: String,
        rating: String,
        token: String
    ){
        binding.progressBar.visible()
        mAddReviewViewModel.addReview(subject_id, review, rating, token).observe(this) { outcome ->
            binding.progressBar.gone()
            when (outcome) {
                is Outcome.Success -> {
                    if (outcome.data.status == 1) {
                        Toast.makeText(this, outcome.data.result.message, Toast.LENGTH_SHORT)
                            .show()
                        finish()
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
}