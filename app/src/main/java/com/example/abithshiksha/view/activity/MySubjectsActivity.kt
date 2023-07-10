package com.example.abithshiksha.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.abithshiksha.R
import com.example.abithshiksha.databinding.ActivityMcqSetsBinding
import com.example.abithshiksha.databinding.ActivityMySubjectsBinding
import com.example.abithshiksha.helper.PrefManager
import com.example.abithshiksha.model.pojo.TestModel
import com.example.abithshiksha.model.pojo.get_course_subject.CartSubjectDetail
import com.example.abithshiksha.model.repo.Outcome
import com.example.abithshiksha.view.adapter.PastCoursesAdapter
import com.example.abithshiksha.view_model.GetCartDetailsViewModel
import com.example.abithshiksha.view_model.GetCourseSubjectViewModel
import com.thekhaeng.pushdownanim.PushDownAnim
import com.user.caregiver.gone
import com.user.caregiver.isConnectedToInternet
import com.user.caregiver.lightStatusBar
import com.user.caregiver.visible
import org.koin.androidx.viewmodel.ext.android.viewModel

class MySubjectsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMySubjectsBinding
    private lateinit var accessToken: String
    private var id: Int = 0
    private val mGetCourseSubjectViewModel: GetCourseSubjectViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMySubjectsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lightStatusBar(this,R.color.white)
        val extras = intent.extras
        if (extras != null) {
            val cart_id = intent?.getIntExtra("id",0)
            id = cart_id!!
        }

        //get token
        accessToken = "Bearer "+ PrefManager.getKeyAuthToken()

        PushDownAnim.setPushDownAnimTo(binding.backArrow).setOnClickListener {
            finish()
        }

        if(isConnectedToInternet()){
            getSubjects(accessToken, id)
        }else{
            Toast.makeText(this,"No internet connection.",Toast.LENGTH_LONG).show()
        }
    }

    private fun fillPastCoursesRecycler(list: List<CartSubjectDetail>) {
        val gridLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.pastCoursesRecycler.apply {
            layoutManager = gridLayoutManager
            adapter = PastCoursesAdapter(list,this@MySubjectsActivity)
        }
    }

    private fun getSubjects(
        token: String,
        cart_id: Int
    ){
        binding.pastCoursesRecycler.gone()
        binding.subjectShimmerView.visible()
        binding.subjectShimmerView.startShimmer()
        binding.noDataLot.gone()
        binding.noDataTv.gone()
        mGetCourseSubjectViewModel.getSubjects(token,cart_id).observe(this) { outcome ->
            when (outcome) {
                is Outcome.Success -> {
                    if (outcome.data.status == 1) {
                        binding.subjectShimmerView.gone()
                        binding.subjectShimmerView.stopShimmer()

                        if(outcome.data.result.carts.cart_subject_details.size != 0 && outcome.data.result.carts.cart_subject_details != null){
                            binding.pastCoursesRecycler.visible()
                            binding.noDataLot.gone()
                            binding.noDataTv.gone()
                            fillPastCoursesRecycler(outcome.data.result.carts.cart_subject_details)

                        }else{
                            binding.pastCoursesRecycler.gone()
                            binding.noDataLot.visible()
                            binding.noDataTv.visible()
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
}