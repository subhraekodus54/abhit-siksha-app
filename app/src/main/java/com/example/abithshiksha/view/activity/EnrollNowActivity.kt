package com.example.abithshiksha.view.activity

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.abithshiksha.R
import com.example.abithshiksha.databinding.ActivityEnrollNowBinding
import com.example.abithshiksha.databinding.ActivityLessonsBinding
import com.example.abithshiksha.helper.CourseSelectListener
import com.example.abithshiksha.helper.PrefManager
import com.example.abithshiksha.helper.click_listener.AddOnsClickListener
import com.example.abithshiksha.model.pojo.add_ons.Addon
import com.example.abithshiksha.model.pojo.get_filtered_subject.Subject
import com.example.abithshiksha.model.repo.Outcome
import com.example.abithshiksha.view.adapter.AddOnsListAdapter
import com.example.abithshiksha.view.adapter.SelectSubjectAdapter
import com.example.abithshiksha.view_model.AddToCartViewModel
import com.example.abithshiksha.view_model.GetAddonsViewModel
import com.example.abithshiksha.view_model.GetFilteredSubjectViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.thekhaeng.pushdownanim.PushDownAnim
import com.user.caregiver.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class EnrollNowActivity : AppCompatActivity(), CourseSelectListener, AddOnsClickListener {
    private lateinit var binding: ActivityEnrollNowBinding
    private lateinit var accessToken: String
    private val mGetFilteredSubjectViewModel: GetFilteredSubjectViewModel by viewModel()
    private val mAddToCartViewModel: AddToCartViewModel by viewModel()
    private val mGetAddonsViewModel: GetAddonsViewModel by viewModel()

    var subjectList: MutableList<Int> = mutableListOf()
    private var subject_price: Int = 0
    private var subject_count: Int = 0
    private var course_type: Int = 0
    private var subject_id: Int = 0
    var addOnsList: MutableList<Addon> =  mutableListOf()

    lateinit var adapter: SelectSubjectAdapter
    private var board: String = ""
    private var boardId: String = ""

    private var standard: String = ""
    private var standardId: String = ""

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
            boardId = intent?.getStringExtra("board_id").toString()
            standardId = intent?.getStringExtra("class_id").toString()
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
                                val list: MutableList<Int> = mutableListOf()
                                for (i in addOnsList){
                                    if (i.isSelected == true){
                                        list.add(i.id)
                                    }
                                }
                                addToCart(course_type,subjectList,accessToken,1, list)
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
                                val list: MutableList<Int> = mutableListOf()
                                for (i in addOnsList){
                                    if (i.isSelected == true){
                                        list.add(i.id)
                                    }
                                }
                                addToCart(course_type,subjectList,accessToken,0, list)
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


        PushDownAnim.setPushDownAnimTo(binding.addOnBtn).setOnClickListener {
            showAddOnsBottomSheet()
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
        getAddons(accessToken, boardId.toInt(), standard)
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
        getAddons(accessToken, boardId.toInt(), standard)
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

                            if(outcome.data.result.result.subjects != null && outcome.data.result.result.subjects.size > 0){
                                binding.addOnBtn.visible()
                            }else{
                                binding.addOnBtn.gone()
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
                            binding.addOnBtn.gone()
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
        isBuy: Int,
        addOns: List<Int>
    ){
        val loader = this.loadingDialog()
        loader.show()
        mAddToCartViewModel.addToCart(course_type, subjects, isBuy, addOns, token).observe(this) { outcome ->
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

            binding.addOnBtn.visible()

        }else{
            subject_price -= price
            subject_count -= 1
            subjectList.remove(id)

            if(subjectList.size < 1){
                binding.addOnBtn.gone()
            }else{
                binding.addOnBtn.visible()
            }
        }
        binding.priceTv.text = "₹${subject_price.toString()}"
    }

    private fun getAddons(
        token: String,
        board_id: Int,
        standard: String
    ){
        mGetAddonsViewModel.getAddons(token, board_id, standard).observe(this) { outcome ->
            when (outcome) {
                is Outcome.Success -> {
                    if (outcome.data.status == 1) {
                        addOnsList = mutableListOf()
                        if(outcome.data.result.addons.size != 0 && outcome.data.result.addons != null){
                            addOnsList.addAll(outcome.data.result.addons)
                        }
                    }else{
                        Toast.makeText(this, outcome.data.result.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                is Outcome.Failure<*> -> {
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT)
                        .show()
                    Log.i("statusMsg", outcome.e.message.toString())

                    outcome.e.printStackTrace()
                    Log.i("status", outcome.e.cause.toString())
                }
            }
        }
    }

    private fun showAddOnsBottomSheet(){
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.add_ons_bottomsheet_layout, null)

        val btnClear = view.findViewById<ImageView>(R.id.clear_btn)
        val coursesRecycler = view.findViewById<RecyclerView>(R.id.add_ons_recycler)

        //setup recycler
        fillAddOnRecyclerView(addOnsList, coursesRecycler)

        //care type select
        btnClear.setOnClickListener {
            dialog.dismiss()
        }

        dialog.setCancelable(true)
        dialog.setContentView(view)
        dialog.show()
    }

    private fun fillAddOnRecyclerView(list: List<Addon>, recyclerView: RecyclerView) {
        val linearLayoutManager = LinearLayoutManager(this)
        recyclerView.apply {
            layoutManager = linearLayoutManager
            setHasFixedSize(true)
            isFocusable = false
            adapter = AddOnsListAdapter(list.toMutableList(),this@EnrollNowActivity, this@EnrollNowActivity)
        }
    }

    override fun onClick(view: View, id: Int, isChecked: Boolean, price: Int) {
        addOnsList.find { it.id == id }?.isSelected = isChecked
        if(isChecked){
            subject_price += price
        }else{
            subject_price -= price
        }
        binding.priceTv.text = "₹${subject_price}"
    }
}