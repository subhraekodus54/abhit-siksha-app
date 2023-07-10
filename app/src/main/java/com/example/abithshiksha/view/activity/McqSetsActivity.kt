package com.example.abithshiksha.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.abithshiksha.R
import com.example.abithshiksha.databinding.ActivityChapterContentBinding
import com.example.abithshiksha.databinding.ActivityMcqSetsBinding
import com.example.abithshiksha.helper.PrefManager
import com.example.abithshiksha.model.repo.Outcome
import com.example.abithshiksha.view.adapter.LessonsListAdapter
import com.example.abithshiksha.view.adapter.McqSetsAdapter
import com.example.abithshiksha.view_model.GetMcqSetsViewModel
import com.example.abithshiksha.view_model.GetTopicViewModel
import com.user.caregiver.gone
import com.user.caregiver.isConnectedToInternet
import com.user.caregiver.lightStatusBar
import com.user.caregiver.visible
import org.koin.androidx.viewmodel.ext.android.viewModel

class McqSetsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMcqSetsBinding
    private val mGetMcqSetsViewModel: GetMcqSetsViewModel by viewModel()
    private lateinit var accessToken: String
    private var lesson_name: String = "No data found"
    private var lesson_id: Int = 0
    lateinit var adapter: McqSetsAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMcqSetsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lightStatusBar(this,R.color.white)
        binding.noDataImg.gone()
        binding.noDataHtv.gone()

        val extras = intent.extras
        if (extras != null) {
            lesson_id = intent?.getIntExtra("id",0)!!
            lesson_name = intent?.getStringExtra("lesson_name").toString()
        }

        //get token
        accessToken = "Bearer "+ PrefManager.getKeyAuthToken()

        binding.backArrow.setOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        if(isConnectedToInternet()){
            val layoutManager = LinearLayoutManager(this)
            binding.mcqRecycler.layoutManager = layoutManager
            adapter = McqSetsAdapter(mutableListOf(),this)
            binding.mcqRecycler.adapter = adapter
            getMcqSets(accessToken,lesson_id)
        }else{
            Toast.makeText(this,"No internet connection.",Toast.LENGTH_LONG).show()
        }
        super.onResume()
    }

    private fun getMcqSets(
        token: String,
        lesson_id: Int
    ){
        binding.mcqRecycler.gone()
        binding.mcqShimmerView.visible()
        binding.mcqShimmerView.startShimmer()
        mGetMcqSetsViewModel.getMcqSets(token, lesson_id).observe(this) { outcome ->
            when (outcome) {
                is Outcome.Success -> {
                    if (outcome.data.status == 1) {
                        binding.mcqShimmerView.gone()
                        binding.mcqShimmerView.stopShimmer()
                        if(outcome.data.result.result.isNotEmpty()){
                            binding.mcqRecycler.visible()
                            adapter.add(outcome.data.result.result)
                            binding.noDataImg.gone()
                            binding.noDataHtv.gone()
                        }else{
                            Toast.makeText(this, "No data found", Toast.LENGTH_SHORT)
                                .show()
                            binding.noDataImg.visible()
                            binding.noDataHtv.visible()
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