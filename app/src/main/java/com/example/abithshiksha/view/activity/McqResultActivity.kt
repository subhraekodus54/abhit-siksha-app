package com.example.abithshiksha.view.activity

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.abithshiksha.R
import com.example.abithshiksha.databinding.ActivityMcqResultBinding
import com.example.abithshiksha.databinding.ActivityTestPlayBinding
import com.example.abithshiksha.helper.PrefManager
import com.example.abithshiksha.model.repo.Outcome
import com.example.abithshiksha.view_model.GetMcqResultViewModel
import com.example.abithshiksha.view_model.GetMcqViewModel
import com.user.caregiver.gone
import com.user.caregiver.isConnectedToInternet
import com.user.caregiver.lightStatusBar
import com.user.caregiver.visible
import org.eazegraph.lib.models.PieModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class McqResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMcqResultBinding
    private var test_id:Int = 0
    private val mGetMcqResultViewModel: GetMcqResultViewModel by viewModel()
    private lateinit var accessToken: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMcqResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lightStatusBar(this,R.color.white)

        val extras = intent.extras
        if (extras != null) {
            test_id = intent?.getIntExtra("id",0)!!
        }

        //get token
        accessToken = "Bearer "+ PrefManager.getKeyAuthToken()

        binding.backArrow.setOnClickListener {
            finish()
        }

        if(isConnectedToInternet()){
            getMcqResult(accessToken,test_id)
        }else{
            Toast.makeText(this,"No internet connection.",Toast.LENGTH_LONG).show()
        }
    }

    private fun showChart(
        correct: Int,
        incorrect: Int,
        unattempted: Int
    ){
        //correct
        binding.pieChart.addPieSlice(
            PieModel(
                "Correct", correct.toFloat(),
                Color.parseColor("#66BB6A")
            )
        )

        //incorrect
        binding.pieChart.addPieSlice(
            PieModel(
                "Incorrect", incorrect.toFloat(),
                Color.parseColor("#FFA726")
            )
        )

        //unattempted
        binding.pieChart.addPieSlice(
            PieModel(
                "Unattempted", unattempted.toFloat(),
                Color.parseColor("#EF5350")
            )
        )
        binding.pieChart.startAnimation()
    }

    private fun getMcqResult(
        token: String,
        id: Int
    ){
        mGetMcqResultViewModel.getMcqResult(token, id).observe(this) { outcome ->
            when (outcome) {
                is Outcome.Success -> {
                    if (outcome.data.status == 1) {
                        val unattempted: Int = outcome.data.result.result.total_question - outcome.data.result.result.attempted_question
                        showChart(outcome.data.result.result.correct_attempted,outcome.data.result.result.incorrect_attempted,unattempted)
                        binding.totalQTv.text = outcome.data.result.result.total_question.toString()
                        binding.correctTv.text = outcome.data.result.result.correct_attempted.toString()
                        binding.incorrectTv.text = outcome.data.result.result.incorrect_attempted.toString()
                        binding.attemptedTv.text = outcome.data.result.result.attempted_question.toString()
                        binding.unattemptedTv.text = unattempted.toString()

                    } else {
                        Toast.makeText(this, outcome.data.result.message, Toast.LENGTH_SHORT).show()
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