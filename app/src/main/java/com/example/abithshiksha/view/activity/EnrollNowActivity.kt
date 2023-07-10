package com.example.abithshiksha.view.activity

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.abithshiksha.R
import com.example.abithshiksha.databinding.ActivityEnrollNowBinding
import com.example.abithshiksha.databinding.ActivityLessonsBinding
import com.example.abithshiksha.helper.CourseSelectListener
import com.example.abithshiksha.helper.PrefManager
import com.example.abithshiksha.model.pojo.get_filtered_subject.Subject
import com.example.abithshiksha.model.repo.Outcome
import com.example.abithshiksha.view.adapter.SelectSubjectAdapter
import com.example.abithshiksha.view_model.AddToCartViewModel
import com.example.abithshiksha.view_model.GetFilteredSubjectViewModel
import com.thekhaeng.pushdownanim.PushDownAnim
import com.user.caregiver.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class EnrollNowActivity : AppCompatActivity(), CourseSelectListener {
    private lateinit var binding: ActivityEnrollNowBinding
    private lateinit var accessToken: String
    private val mGetFilteredSubjectViewModel: GetFilteredSubjectViewModel by viewModel()
    private val mAddToCartViewModel: AddToCartViewModel by viewModel()

    var subjectList: MutableList<Int> = mutableListOf()
    private var subject_price: Int = 0
    private var subject_count: Int = 0
    private var course_type: Int = 0
    private var subject_id: Int = 0

    lateinit var adapter: SelectSubjectAdapter
    private var board: String = ""
    private var standard: String = ""
    private var stream: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEnrollNowBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lightStatusBar(this,R.color.white)

        val extras = intent.extras
        if (extras != null) {
            board = intent?.getStringExtra("board").toString()
            standard = intent?.getStringExtra("class").toString()
            subject_id = intent?.getIntExtra("subject_id",0)!!

            board?.let {
                binding.boardTv.text = it
            }

            standard?.let {
                binding.classTv.text = it
            }
        }

        //get token
        accessToken = "Bearer "+ PrefManager.getKeyAuthToken()

        binding.subjectShimmerView.visible()
        binding.subjectShimmerView.startShimmer()
        binding.subjectRecycler.gone()

        binding.streamHtv.gone()
        binding.streamLay.gone()

        binding.priceTv.text = "₹${subject_price.toString()}"
        selectCustomPackage()

        //full package
        binding.fullLay.setOnClickListener {
            selectFullPackage()
        }

        //custom package
        binding.customLay.setOnClickListener {
            selectCustomPackage()
        }

        PushDownAnim.setPushDownAnimTo(binding.backArrow).setOnClickListener {
            finish()
        }

        binding.countTv.setOnClickListener {
            if(board.isNotEmpty()){
                if(standard.toInt() != 0){
                    if(course_type != 0){
                        if(subjectList.size > 0){
                            if(isConnectedToInternet()){
                                addToCart(course_type,subjectList,accessToken,1)
                            }else{
                                Toast.makeText(this,"No internet connection.",Toast.LENGTH_LONG).show()
                            }
                        }else{
                            Toast.makeText(this,"please select a subject.",Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        Toast.makeText(this,"please select a package.",Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(this,"please select a class.",Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this,"please select a board.",Toast.LENGTH_SHORT).show()
            }
        }

        binding.addToCartBtn.setOnClickListener {
            if(board.isNotEmpty()){
                if(standard.toInt() != 0){
                    if(course_type != 0){
                        if(subjectList.size > 0){
                            if(isConnectedToInternet()){
                                addToCart(course_type,subjectList,accessToken,0)
                            }else{
                                Toast.makeText(this,"No internet connection.",Toast.LENGTH_LONG).show()
                            }
                        }else{
                            Toast.makeText(this,"please select a subject.",Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        Toast.makeText(this,"please select a package.",Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(this,"please select a class.",Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this,"please select a board.",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun selectFullPackage(){
        binding.fullLay.background = ContextCompat.getDrawable(this, R.drawable.btn_bg_1)
        binding.customLay.background = ContextCompat.getDrawable(this, R.drawable.course_bg_one)
        binding.fullTv.setTextColor(Color.parseColor("#FFFFFF"))
        binding.customTv.setTextColor(Color.parseColor("#000000"))
        binding.selectSubjectHtv.visible()
        binding.subjectShimmerView.visible()
        adapter = SelectSubjectAdapter(mutableListOf(),this,false,this, subject_id, true)
        binding.subjectRecycler.adapter = adapter
        subjectList = mutableListOf()
        course_type = 1
        binding.noDataLot.gone()
        binding.noSubjectTv.gone()
        getFilteredSubject(board,standard,stream,accessToken,false)
    }

    private fun selectCustomPackage(){
        binding.customLay.background = ContextCompat.getDrawable(this, R.drawable.btn_bg_1)
        binding.fullLay.background = ContextCompat.getDrawable(this, R.drawable.course_bg_one)
        binding.customTv.setTextColor(Color.parseColor("#FFFFFF"))
        binding.fullTv.setTextColor(Color.parseColor("#000000"))
        binding.selectSubjectHtv.visible()
        binding.subjectShimmerView.visible()
        adapter = SelectSubjectAdapter(mutableListOf(),this,true,this, subject_id, true)
        binding.subjectRecycler.adapter = adapter
        subjectList = mutableListOf()
        course_type = 2
        binding.noDataLot.gone()
        binding.noSubjectTv.gone()
        getFilteredSubject(board,standard,stream,accessToken,true)
    }

    private fun getFilteredSubject(
        board: String,
        standard: String,
        stream: String,
        token: String,
        isCustom: Boolean,
    ){
        binding.subjectShimmerView.visible()
        binding.subjectShimmerView.startShimmer()
        mGetFilteredSubjectViewModel.getFilteredSubject(board, standard, stream, token).observe(this) { outcome ->
            when (outcome) {
                is Outcome.Success -> {
                    if (outcome.data.status == 1) {
                        binding.subjectShimmerView.stopShimmer()
                        binding.subjectShimmerView.gone()
                        if(isCustom == false){
                            subject_count  = outcome.data.result.result.subjects.size
                            subject_price = outcome.data.result.result.total_amount
                            binding.priceTv.text = "₹"+subject_price.toString()
                            //binding.countTv.text = subject_count.toString()
                            for (i in outcome.data.result.result.subjects){
                                subjectList.add(i.id)
                            }
                        }else{

                            outcome.data.result.result.subjects.filter {
                                it.id == subject_id
                            }.single()?.let {
                                subject_count = 1
                                subject_price = it.subject_amount
                            }

                            /*subject_count  = 0
                            subject_price = 0*/
                            binding.priceTv.text = "₹"+subject_price.toString()
                            //binding.countTv.text = subject_count.toString()
                            subjectList = mutableListOf()
                            subjectList.add(subject_id)
                        }
                        if(outcome.data.result.result.subjects.isNotEmpty()){
                            binding.subjectRecycler.visible()
                            fillSuggestionRecyclerView(outcome.data.result.result.subjects, isCustom)
                        }else{
                            binding.subjectRecycler.gone()
                            binding.noDataLot.visible()
                            binding.noSubjectTv.visible()
                        }

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

    private fun fillSuggestionRecyclerView(list: List<Subject>, isCustom: Boolean) {
        val gridLayoutManager = GridLayoutManager(this, 2)
        binding.subjectRecycler.apply {
            layoutManager = gridLayoutManager
            setHasFixedSize(true)
            isFocusable = false
            adapter = SelectSubjectAdapter(list,this@EnrollNowActivity, isCustom, this@EnrollNowActivity, subject_id, true)
        }
    }

    private fun addToCart(
        course_type: Int,
        subjects: List<Int>,
        token: String,
        isBuy: Int
    ){
        val loader = this.loadingDialog()
        loader.show()
        mAddToCartViewModel.addToCart(course_type, subjects, isBuy, token).observe(this) { outcome ->
            loader.dismiss()
            when (outcome) {
                is Outcome.Success -> {
                    if (outcome.data.status == 1) {
                        if(isBuy == 1){

                            val intent = Intent(this, CartDetailsActivity::class.java)
                            intent.putExtra("id",outcome.data.result.cart_id)
                            startActivity(intent)
                            finish()

                        }else{
                            val intent = Intent(this, CartActivity::class.java)
                            startActivity(intent)
                            finish()
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

    override fun onSelect(view: View, price: Int, isSelected: Boolean, id: Int) {
        if(isSelected){
            subject_price += price
            subject_count += 1
            subjectList.add(id)
        }else{
            subject_price -= price
            subject_count -= 1
            subjectList.remove(id)
        }
        binding.priceTv.text = "₹${subject_price.toString()}"
    }
}