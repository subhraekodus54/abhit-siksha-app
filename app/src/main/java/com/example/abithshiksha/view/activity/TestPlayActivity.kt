package com.example.abithshiksha.view.activity

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.abithshiksha.R
import com.example.abithshiksha.databinding.ActivityTestPlayBinding
import com.example.abithshiksha.helper.PrefManager
import com.example.abithshiksha.model.pojo.submit_mcq.request.Answer
import com.example.abithshiksha.model.repo.Outcome
import com.example.abithshiksha.view_model.GetMcqViewModel
import com.example.abithshiksha.view_model.SubmitMcqViewModel
import com.thekhaeng.pushdownanim.PushDownAnim
import com.user.caregiver.*
import org.eazegraph.lib.charts.PieChart
import org.eazegraph.lib.models.PieModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*


class TestPlayActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTestPlayBinding
    private val mGetMcqViewModel: GetMcqViewModel by viewModel()
    private val mSubmitMcqViewModel: SubmitMcqViewModel by viewModel()
    private val mcqList:MutableList<Answer> = mutableListOf()

    private var set_id: Int = 0
    private var page: Int = 1
    private var lastPage: Int =0
    private lateinit var accessToken: String

    private var selectedQ = ""
    private var selectedA = ""
    private var startTime = ""
    private var endTime = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestPlayBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lightStatusBar(this,R.color.white)

        val extras = intent.extras
        if (extras != null) {
            set_id = intent?.getIntExtra("set_id",0)!!
        }

        //get token
        accessToken = "Bearer "+ PrefManager.getKeyAuthToken()

        val simpleDateFormate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        startTime = simpleDateFormate.format(Date())

        PushDownAnim.setPushDownAnimTo(binding.backArrow).setOnClickListener {
            finish()
        }

        if(isConnectedToInternet()){
            getMcq(accessToken,set_id,page)
        }else{
            Toast.makeText(this,"No internet connection.",Toast.LENGTH_SHORT).show()
        }

        //answer click
        PushDownAnim.setPushDownAnimTo(binding.option1Card).setOnClickListener {
            selectedA = binding.option1Tv.text.toString()
            binding.option1Card.background = ContextCompat.getDrawable(this, R.color.orange)
            binding.option2Card.background = ContextCompat.getDrawable(this, R.color.white)
            binding.option3Card.background = ContextCompat.getDrawable(this, R.color.white)
            binding.option4Card.background = ContextCompat.getDrawable(this, R.color.white)
        }
        PushDownAnim.setPushDownAnimTo(binding.option2Card).setOnClickListener {
            selectedA = binding.option2Tv.text.toString()
            binding.option1Card.background = ContextCompat.getDrawable(this, R.color.white)
            binding.option2Card.background = ContextCompat.getDrawable(this, R.color.orange)
            binding.option3Card.background = ContextCompat.getDrawable(this, R.color.white)
            binding.option4Card.background = ContextCompat.getDrawable(this, R.color.white)
        }
        PushDownAnim.setPushDownAnimTo(binding.option3Card).setOnClickListener {
            selectedA = binding.option3Tv.text.toString()
            binding.option1Card.background = ContextCompat.getDrawable(this, R.color.white)
            binding.option2Card.background = ContextCompat.getDrawable(this, R.color.white)
            binding.option3Card.background = ContextCompat.getDrawable(this, R.color.orange)
            binding.option4Card.background = ContextCompat.getDrawable(this, R.color.white)
        }
        PushDownAnim.setPushDownAnimTo(binding.option4Card).setOnClickListener {
            selectedA = binding.option4Tv.text.toString()
            binding.option1Card.background = ContextCompat.getDrawable(this, R.color.white)
            binding.option2Card.background = ContextCompat.getDrawable(this, R.color.white)
            binding.option3Card.background = ContextCompat.getDrawable(this, R.color.white)
            binding.option4Card.background = ContextCompat.getDrawable(this, R.color.orange)
        }

        PushDownAnim.setPushDownAnimTo(binding.nextCard).setOnClickListener {
            if(selectedA.isNotEmpty()){
                if(page<lastPage){
                    page++
                    mcqList.add(Answer(
                        selectedQ,
                        selectedA
                    ))
                    getMcq(accessToken,set_id,page)
                    if(page == lastPage){
                        binding.nextCard.gone()
                        binding.skipBtn.gone()
                    }
                }
            }else{
                Toast.makeText(this,"Please select an option.",Toast.LENGTH_SHORT).show()
            }
        }

        PushDownAnim.setPushDownAnimTo(binding.skipBtn).setOnClickListener {
            if(page<lastPage){
                page++
                getMcq(accessToken,set_id,page)
                if(page == lastPage){
                    binding.nextCard.gone()
                    binding.skipBtn.gone()
                }
            }
        }

        PushDownAnim.setPushDownAnimTo(binding.submitBtn).setOnClickListener {
            submitTest()
        }

        PushDownAnim.setPushDownAnimTo(binding.cancelBtn).setOnClickListener {
            showCancelPopUp()
        }
    }

    private fun getMcq(
        token: String,
        set_id: Int,
        page: Int
    ){  binding.testMainLay.gone()
        binding.testShimmerView.visible()
        binding.testShimmerView.startShimmer()
        mGetMcqViewModel.getMcq(token, set_id, page).observe(this) { outcome ->
            when (outcome) {
                is Outcome.Success -> {
                    if (outcome.data.status == 1) {
                        binding.questionTv.text = outcome.data.result.result.mcq_question.question
                        outcome.data.result.result.mcq_question.options?.let {
                            binding.testMainLay.visible()
                            binding.testShimmerView.gone()
                            binding.testShimmerView.stopShimmer()
                            //adapter.add(outcome.data.result.result.mcq_question.options)
                            binding.option1Tv.text = it[0]
                            binding.option2Tv.text = it[1]
                            binding.option3Tv.text = it[2]
                            binding.option4Tv.text = it[3]
                        }
                        lastPage = outcome.data.result.result.total_question
                        blankAnswer()
                        selectedQ = outcome.data.result.result.mcq_question.id.toString()
                        selectedA = ""

                    } else {
                        Toast.makeText(this, outcome.data.result.message, Toast.LENGTH_SHORT)
                            .show()
                        binding.testMainLay.gone()
                        binding.testShimmerView.visible()
                        binding.testShimmerView.startShimmer()
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

    private fun submitMcq(
        set_id: String,
        start_time: String,
        end_time: String,
        answers: MutableList<Answer>,
        token: String
    ){  val loader = this.loadingDialog(false,R.raw.no_subject)
        loader.show()
        mSubmitMcqViewModel.submitMcq(set_id,start_time,end_time,answers,token).observe(this) { outcome ->
            loader.dismiss()
            when (outcome) {
                is Outcome.Success -> {
                    if (outcome.data.status == 1) {
                        Toast.makeText(this, outcome.data.result.message, Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, McqResultActivity::class.java)
                        intent.putExtra("id",outcome.data.result.result.user_practice_test_id)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, outcome.data.result.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                is Outcome.Failure<*> -> {
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
                    Log.i("statusMsg", outcome.e.message.toString())

                    outcome.e.printStackTrace()
                    Log.i("status", outcome.e.cause.toString())
                }
            }
        }
    }

    private fun blankAnswer(){
        selectedQ = ""
        selectedA = ""
        binding.option1Card.background = ContextCompat.getDrawable(this, R.color.white)
        binding.option2Card.background = ContextCompat.getDrawable(this, R.color.white)
        binding.option3Card.background = ContextCompat.getDrawable(this, R.color.white)
        binding.option4Card.background = ContextCompat.getDrawable(this, R.color.white)
    }

    private fun submitTest(){

        if(selectedA.isNotEmpty()){
            mcqList.add(Answer(
                selectedQ,
                selectedA
            ))

            //end time
            val simpleDateFormate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            endTime = simpleDateFormate.format(Date())

            //upload api call
            submitMcq(set_id.toString(),startTime,endTime,mcqList,accessToken)

        }else{
            if(mcqList.isEmpty()){
                Toast.makeText(this,"You have not attempted any question",Toast.LENGTH_SHORT).show()
            }else{
                val simpleDateFormate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                endTime = simpleDateFormate.format(Date())

                submitMcq(set_id.toString(),startTime,endTime,mcqList,accessToken)
            }
        }
    }

    private fun showCancelPopUp(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Cancel Test")
        builder.setMessage("Do you want to cancel the test ?")
        builder.setIcon(R.drawable.ic_baseline_warning_24)
        builder.setPositiveButton("Yes"){dialogInterface, which ->
            finish()
        }
        builder.setNegativeButton("No"){dialogInterface, which ->
            //todo cancel
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

}