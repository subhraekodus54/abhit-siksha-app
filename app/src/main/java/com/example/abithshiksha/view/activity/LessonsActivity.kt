package com.example.abithshiksha.view.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.abithshiksha.R
import com.example.abithshiksha.databinding.ActivityLessonsBinding
import com.example.abithshiksha.helper.PaginationScrollListener
import com.example.abithshiksha.helper.PrefManager
import com.example.abithshiksha.helper.click_listener.TopicClickListener
import com.example.abithshiksha.model.repo.Outcome
import com.example.abithshiksha.view.adapter.LessonsListAdapter
import com.example.abithshiksha.view.fragment.TopicBottomSheetFragment
import com.example.abithshiksha.view_model.GetLessonsViewModel
import com.user.caregiver.gone
import com.user.caregiver.isConnectedToInternet
import com.user.caregiver.lightStatusBar
import com.user.caregiver.visible
import org.koin.androidx.viewmodel.ext.android.viewModel

class LessonsActivity : AppCompatActivity(), TopicClickListener {
    private lateinit var binding: ActivityLessonsBinding
    private lateinit var accessToken: String
    private var id:Int = 0
    private var subject_name: String = "No Data Found"
    private val mGetLessonsViewModel: GetLessonsViewModel by viewModel()

    var isLastPage: Boolean = false
    var isLoading: Boolean = false
    var page_no = 1
    lateinit var adapter: LessonsListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLessonsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lightStatusBar(this,R.color.lessons_color)
        binding.noDataImg.gone()
        binding.noDataHtv.gone()
        //get token
        accessToken = "Bearer "+ PrefManager.getKeyAuthToken()

        val extras = intent.extras
        if (extras != null) {
            val subject_id = intent?.getIntExtra("subject_id",0)
            subject_name = intent?.getStringExtra("subject_name").toString()
            id = subject_id!!

        }

        binding.subjectHtv.text = subject_name

        binding.backArrow.setOnClickListener {
            finish()
        }
        //getLessons(accessToken,id,1)

        val layoutManager = LinearLayoutManager(this)
        binding.lessonsRecycler.layoutManager = layoutManager
        binding.lessonsRecycler?.addOnScrollListener(object : PaginationScrollListener(layoutManager) {
            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }

            override fun loadMoreItems() {
                isLoading = true
                page_no++
                getLessons(accessToken,id,page_no)
            }
        })
    }

    override fun onStart() {
        super.onStart()
        //recycle view pagination
        binding.lessonsRecycler.gone()
        binding.lessonsShimmerView.visible()
        binding.lessonsShimmerView.startShimmer()
        adapter = LessonsListAdapter(mutableListOf(),this, this)
        binding.lessonsRecycler.adapter = adapter
        if(isConnectedToInternet()){
            page_no = 1
            getLessons(accessToken,id,page_no)
        }else{
            Toast.makeText(this,"No internet connection.", Toast.LENGTH_LONG).show()
        }
    }

    private fun getLessons(
        token: String,
        subject_id: Int,
        page: Int
    ){
        mGetLessonsViewModel.getLessons(token,subject_id,page).observe(this) { outcome ->
            when (outcome) {
                is Outcome.Success -> {
                    if (outcome.data.status == 1) {
                        binding.lessonsRecycler.visible()
                        binding.lessonsShimmerView.stopShimmer()
                        binding.lessonsShimmerView.gone()

                        if (outcome.data.result.result.data != null && outcome.data.result.result.data.size > 0) {
                            outcome.data.result.result.data?.let {
                                adapter.add(it)
                                isLoading = false
                                binding.noDataImg.gone()
                                binding.noDataHtv.gone()
                                binding.lessonsCountTv.visible()
                            }
                        } else {
                            if (page_no==1){
                                Toast.makeText(this, "No data found", Toast.LENGTH_SHORT)
                                    .show()
                                binding.noDataImg.visible()
                                binding.noDataHtv.visible()
                                binding.lessonsCountTv.gone()
                            }
                        }
                        binding.chapterCountTv.text = outcome.data.result.total_lesson.toString()+" Chapters"
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

    override fun onTopicClick(view: View, id: Int) {
        val bundle = Bundle()
        bundle.putInt("lesson_id", id)
        val fragment = TopicBottomSheetFragment()
        fragment.setArguments(bundle)
        fragment.show(supportFragmentManager, "TAG")
    }
}