package com.example.abithshiksha.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.abithshiksha.R
import com.example.abithshiksha.databinding.ActivityChapterContentBinding
import com.example.abithshiksha.helper.PaginationScrollListener
import com.example.abithshiksha.helper.PrefManager
import com.example.abithshiksha.model.pojo.get_topics.ResultX
import com.example.abithshiksha.model.repo.Outcome
import com.example.abithshiksha.view.adapter.TopicListAdapter
import com.example.abithshiksha.view_model.GetTopicViewModel
import com.user.caregiver.gone
import com.user.caregiver.isConnectedToInternet
import com.user.caregiver.lightStatusBar
import com.user.caregiver.visible
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChapterContentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChapterContentBinding
    private val mGetTopicViewModel: GetTopicViewModel by viewModel()
    private lateinit var accessToken: String
    private var lesson_name: String = "No data found"
    private var lesson_id: Int = 0

    var isLastPage: Boolean = false
    var isLoading: Boolean = false
    var page_no = 1
    lateinit var adapter: TopicListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChapterContentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lightStatusBar(this,R.color.topic_color)
        binding.noDataImg.gone()
        binding.noDataHtv.gone()

        //get token
        accessToken = "Bearer "+ PrefManager.getKeyAuthToken()

        val extras = intent.extras
        if (extras != null) {
            lesson_id = intent?.getIntExtra("id",0)!!
            lesson_name = intent?.getStringExtra("lesson_name").toString()
        }

        //on scroll recycle view
        val layoutManager = LinearLayoutManager(this)
        binding.topicRecycler.layoutManager = layoutManager
        binding.topicRecycler?.addOnScrollListener(object : PaginationScrollListener(layoutManager) {
            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }

            override fun loadMoreItems() {
                isLoading = true
                page_no++
                getTopic(accessToken,lesson_id,page_no)
            }
        })

        binding.backArrow.setOnClickListener {
            finish()
        }
        binding.lessonNameTv.text = lesson_name

        binding.playTestLay.setOnClickListener {
            val intent = Intent(this, McqSetsActivity::class.java)
            intent.putExtra("id",lesson_id)
            intent.putExtra("lesson_name",lesson_name)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        //recycle view pagination
        binding.topicRecycler.gone()
        binding.topicShimmerView.visible()
        binding.topicShimmerView.startShimmer()
        adapter = TopicListAdapter(mutableListOf(),this)
        binding.topicRecycler.adapter = adapter
        if(isConnectedToInternet()){
            page_no = 1
            getTopic(accessToken,lesson_id,page_no)
        }else{
            Toast.makeText(this,"No internet connection.", Toast.LENGTH_LONG).show()
        }
    }

    private fun getTopic(
        token: String,
        lesson_id: Int,
        page: Int
    ){
        mGetTopicViewModel.getTopic(token, lesson_id, page).observe(this) { outcome ->
            when (outcome) {
                is Outcome.Success -> {
                    if (outcome.data.status == 1) {
                        binding.topicRecycler.visible()
                        binding.topicShimmerView.stopShimmer()
                        binding.topicShimmerView.gone()

                        if (outcome.data.result.result != null && outcome.data.result.result.size > 0) {
                            outcome.data.result.result?.let {
                                adapter.add(it)
                                isLoading = false
                                binding.noDataImg.gone()
                                binding.noDataHtv.gone()
                                binding.conceptsHtv.visible()
                            }
                        } else {
                            if (page_no==1){
                                Toast.makeText(this, "No data found", Toast.LENGTH_SHORT)
                                    .show()
                                binding.noDataImg.visible()
                                binding.noDataHtv.visible()
                                binding.conceptsHtv.gone()
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
}