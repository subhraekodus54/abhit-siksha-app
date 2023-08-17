package com.example.abithshiksha.view.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.abithshiksha.R
import com.example.abithshiksha.databinding.ActivityCoursesBinding
import com.example.abithshiksha.helper.CourseSelectListener
import com.example.abithshiksha.helper.PrefManager
import com.example.abithshiksha.helper.click_listener.AddOnsClickListener
import com.example.abithshiksha.model.pojo.add_ons.Addon
import com.example.abithshiksha.model.pojo.get_filtered_subject.Subject
import com.example.abithshiksha.model.repo.Outcome
import com.example.abithshiksha.view.adapter.AddOnsListAdapter
import com.example.abithshiksha.view.adapter.SelectSubjectAdapter
import com.example.abithshiksha.view_model.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.thekhaeng.pushdownanim.PushDownAnim
import com.user.caregiver.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class CoursesActivity : AppCompatActivity(), CourseSelectListener, AddOnsClickListener {
    private lateinit var binding: ActivityCoursesBinding

    var classList: MutableList<String> =  mutableListOf()
    var streamList: Array<String> =  arrayOf("Select Stream","Arts","Commerce","Science")
    var subjectList: MutableList<Int> = mutableListOf()
    var boardList: MutableList<String> =  mutableListOf()
    var addOnsList: MutableList<Addon> =  mutableListOf()
    var boardIdList: MutableList<Int> =  mutableListOf()

    private var class_global: String = ""
    private var std: Int = 0
    private var board = ""
    private var boardId = 0

    private var stream = ""
    private var subject_price: Int = 0
    private var subject_count: Int = 0
    private var course_type: Int = 0
    private val mGetFilteredSubjectViewModel: GetFilteredSubjectViewModel by viewModel()
    private val mAddToCartViewModel: AddToCartViewModel by viewModel()
    private val mGetClassListViewModel: GetClassListViewModel by viewModel()
    private val mGetBoardsViewModel: GetBoardsViewModel by viewModel()
    private val mGetProfileViewModel: GetProfileViewModel by viewModel()
    private val mGetAddonsViewModel: GetAddonsViewModel by viewModel()

    lateinit var adapter: SelectSubjectAdapter
    private lateinit var accessToken: String
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCoursesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lightStatusBar(this,R.color.white)

        //get token
        accessToken = "Bearer "+ PrefManager.getKeyAuthToken()

        binding.priceTv.text = "₹${subject_price.toString()}"

        binding.classSpinnerLay.gone()
        binding.selectPackageLay.gone()
        binding.selectPackageHtv.gone()
        binding.selectSubjectHtv.gone()
        binding.streamSpinnerLay.gone()
        binding.subjectRecycler.gone()
        binding.subjectShimmerView.gone()
        binding.progressBar.gone()
        binding.noDataLot.gone()
        binding.noSubjectTv.gone()
        binding.noClassTv.gone()
        binding.addOnBtn.gone()

        binding.backArrow.setOnClickListener {
            finish()
        }

        if(isConnectedToInternet()){
            getProfile(accessToken)
        }else{
            Toast.makeText(this,"No internet connection.",Toast.LENGTH_SHORT).show()
        }

        binding.countTv.setOnClickListener {
            if(board.isNotEmpty()){
                if(std != 0){
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
                if(std != 0){
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

        //class spinner
        setupClassSpinner()

        //stream spinner
        setupStreamSpinner()

        //full package
        binding.fullLay.setOnClickListener {
            binding.fullLay.background = ContextCompat.getDrawable(this, R.drawable.btn_bg_1)
            binding.customLay.background = ContextCompat.getDrawable(this, R.drawable.course_bg_one)
            binding.fullTv.setTextColor(Color.parseColor("#FFFFFF"))
            binding.customTv.setTextColor(Color.parseColor("#000000"))
            binding.selectSubjectHtv.visible()
            binding.subjectShimmerView.visible()
            adapter = SelectSubjectAdapter(mutableListOf(),this,false,this,0,false)
            binding.subjectRecycler.adapter = adapter
            subjectList = mutableListOf()
            course_type = 1
            binding.noDataLot.gone()
            binding.noSubjectTv.gone()
            getFilteredSubject(board,class_global,stream,accessToken,false)
            if(boardId != 0){
                getAddons(accessToken, boardId, class_global)
            }
        }

        //custom package
        binding.customLay.setOnClickListener {
            binding.customLay.background = ContextCompat.getDrawable(this, R.drawable.btn_bg_1)
            binding.fullLay.background = ContextCompat.getDrawable(this, R.drawable.course_bg_one)
            binding.customTv.setTextColor(Color.parseColor("#FFFFFF"))
            binding.fullTv.setTextColor(Color.parseColor("#000000"))
            binding.selectSubjectHtv.visible()
            binding.subjectShimmerView.visible()
            adapter = SelectSubjectAdapter(mutableListOf(),this,true,this,0,false)
            binding.subjectRecycler.adapter = adapter
            subjectList = mutableListOf()
            course_type = 2
            binding.noDataLot.gone()
            binding.noSubjectTv.gone()
            getFilteredSubject(board,class_global,stream,accessToken,true)
            if(boardId != 0){
                getAddons(accessToken, boardId, class_global)
            }
        }

        binding.cartBtn.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }

        PushDownAnim.setPushDownAnimTo(binding.addOnBtn).setOnClickListener {
            showAddOnsBottomSheet()
        }

        //get board list
        getBoards()

    }

    private fun setupStreamSpinner() {
        val arrayAdapter2 = ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,streamList)
        binding.streamSpinner.adapter = arrayAdapter2
        binding.streamSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener,
            AdapterView.OnItemClickListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                stream = streamList[p2]

                //
                adapter = SelectSubjectAdapter(mutableListOf(),this@CoursesActivity,false,this@CoursesActivity,0,false)
                binding.subjectRecycler.adapter = adapter
                binding.selectSubjectHtv.gone()
                binding.customLay.background = ContextCompat.getDrawable(this@CoursesActivity, R.drawable.course_bg_one)
                binding.fullLay.background = ContextCompat.getDrawable(this@CoursesActivity, R.drawable.course_bg_one)
                binding.customTv.setTextColor(Color.parseColor("#000000"))
                binding.fullTv.setTextColor(Color.parseColor("#000000"))
                subject_count  = 0
                subject_price = 0
                binding.priceTv.text = "₹"+subject_price.toString()
                //binding.countTv.text = subject_count.toString()

                if(p2>0){
                    binding.selectPackageHtv.visible()
                    binding.selectPackageLay.visible()
                    binding.subjectShimmerView.gone()
                    subjectList = mutableListOf()
                    binding.noDataLot.gone()
                    binding.noSubjectTv.gone()

                }else{
                    binding.selectPackageHtv.gone()
                    binding.selectPackageLay.gone()
                    binding.subjectShimmerView.gone()
                    subjectList = mutableListOf()
                    binding.noDataLot.gone()
                    binding.noSubjectTv.gone()

                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                TODO("Not yet implemented")
            }
        }
    }

    private fun setupClassSpinner() {
        val arrayAdapter = ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,classList)
        binding.classSpinner.adapter = arrayAdapter
        binding.classSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener,
            AdapterView.OnItemClickListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                class_global = classList[p2]
                std = p2
                course_type = 0
                //
                adapter = SelectSubjectAdapter(mutableListOf(),this@CoursesActivity,false,this@CoursesActivity,0,false)
                binding.subjectRecycler.adapter = adapter
                binding.selectSubjectHtv.gone()
                binding.customLay.background = ContextCompat.getDrawable(this@CoursesActivity, R.drawable.course_bg_one)
                binding.fullLay.background = ContextCompat.getDrawable(this@CoursesActivity, R.drawable.course_bg_one)
                binding.customTv.setTextColor(Color.parseColor("#000000"))
                binding.fullTv.setTextColor(Color.parseColor("#000000"))
                subject_count  = 0
                subject_price = 0
                binding.priceTv.text = "₹"+subject_price.toString()
                //binding.countTv.text = subject_count.toString()

                if(p2>0){
                    if(p2>10){
                        setupStreamSpinner()
                        binding.streamSpinnerLay.visible()
                        binding.selectPackageHtv.gone()
                        binding.selectPackageLay.gone()
                        binding.subjectShimmerView.gone()
                        subjectList = mutableListOf()
                        binding.noDataLot.gone()
                        binding.noSubjectTv.gone()
                    }else{
                        binding.selectPackageHtv.visible()
                        binding.selectPackageLay.visible()
                        binding.streamSpinnerLay.gone()
                        binding.subjectShimmerView.gone()
                        subjectList = mutableListOf()
                        binding.noDataLot.gone()
                        binding.noSubjectTv.gone()
                    }
                }else{
                    binding.selectPackageHtv.gone()
                    binding.selectPackageLay.gone()
                    binding.streamSpinnerLay.gone()
                    binding.subjectShimmerView.gone()
                    subjectList = mutableListOf()
                    binding.noDataLot.gone()
                    binding.noSubjectTv.gone()
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            }
        }
    }

    private fun fillSuggestionRecyclerView(list: List<Subject>, isCustom: Boolean) {
        val gridLayoutManager = GridLayoutManager(this, 2)
        binding.subjectRecycler.apply {
            layoutManager = gridLayoutManager
            setHasFixedSize(true)
            isFocusable = false
            adapter = SelectSubjectAdapter(list,this@CoursesActivity, isCustom, this@CoursesActivity,0,false)
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

    private fun getFilteredSubject(
        board: String,
        standard: String,
        stream: String,
        token: String,
        isCustom: Boolean
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
                            subject_count  = 0
                            subject_price = 0
                            binding.priceTv.text = "₹"+subject_price.toString()

                            subjectList = mutableListOf()
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
                    Toast.makeText(this, R.string.network_issue, Toast.LENGTH_SHORT).show()
                    Log.i("statusMsg", outcome.e.message.toString())

                    outcome.e.printStackTrace()
                    Log.i("status", outcome.e.cause.toString())
                }
            }
        }
    }

    private fun getClassList(
        board_name: String
    ){
        binding.classSpinnerLay.visible()
        binding.classSpinner.gone()
        binding.noClassTv.gone()
        binding.classShimmerView.visible()
        binding.classShimmerView.startShimmer()
        mGetClassListViewModel.getClassList(accessToken,board_name).observe(this) { outcome ->
            when (outcome) {
                is Outcome.Success -> {
                    if (outcome.data.status == 1) {
                        binding.classShimmerView.stopShimmer()
                        binding.classShimmerView.gone()
                        if(outcome.data.result.result.all_class.isNotEmpty()){
                            for (i in outcome.data.result.result.all_class){
                                classList.add(i)
                            }
                            binding.noClassTv.gone()
                            binding.classSpinner.visible()
                            setupClassSpinner()
                        }else{
                            binding.classSpinner.gone()
                            binding.noClassTv.visible()
                            std = 0
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

    private fun addToCart(
        course_type: Int,
        subjects: List<Int>,
        token: String,
        isBuy: Int,
        addons: List<Int>
    ){
        val loader = this.loadingDialog()
        loader.show()
        mAddToCartViewModel.addToCart(course_type, subjects, isBuy, addons, token).observe(this) { outcome ->
            loader.dismiss()
            when (outcome) {
                is Outcome.Success -> {
                    if (outcome.data.status == 1) {
                        if(isBuy == 1){

                            val intent = Intent(this, CartDetailsActivity::class.java)
                            intent.putExtra("id",outcome.data.result.cart_id)
                            startActivity(intent)

                        }else{
                            val intent = Intent(this, CartActivity::class.java)
                            startActivity(intent)
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

    private fun getBoards(){
        boardList = mutableListOf()
        binding.boardSpinner.gone()
        binding.noBoardTv.gone()
        binding.boardShimmerView.visible()
        binding.boardShimmerView.startShimmer()
        mGetBoardsViewModel.getBoards().observe(this) { outcome ->

            when (outcome) {
                is Outcome.Success -> {
                    if (outcome.data.status == 1) {
                        binding.boardShimmerView.gone()
                        binding.boardShimmerView.stopShimmer()

                        if(outcome.data.result.board != null && outcome.data.result.board.size > 0){

                            boardList.add("Select Board")
                            binding.boardSpinner.visible()
                            binding.noBoardTv.gone()

                            for (i in outcome.data.result.board){
                                boardList.add(i.exam_board)
                            }

                            for (i in outcome.data.result.board){
                                boardIdList.add(i.id)
                            }
                            setupBoardSpinner(boardList, boardIdList)
                        }else{
                            binding.boardSpinner.gone()
                            binding.noBoardTv.visible()
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

    private fun setupBoardSpinner(boardList: List<String>, boardIdList: List<Int>) {
        val arrayAdapter2 = ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,boardList)
        binding.boardSpinner.adapter = arrayAdapter2
        binding.boardSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener,
            AdapterView.OnItemClickListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                board = boardList[p2]

                if(p2 != 0){
                    classList = mutableListOf()
                    getClassList(board)

                    binding.selectPackageHtv.gone()
                    binding.selectPackageLay.gone()
                    binding.streamSpinnerLay.gone()
                    binding.selectSubjectHtv.gone()
                    binding.subjectRecycler.gone()
                    //setupClassSpinner()
                    subjectList = mutableListOf()
                    binding.noDataLot.gone()
                    binding.noSubjectTv.gone()
                    course_type = 0
                    boardId = boardIdList[p2-1]

                }else{
                    //Toast.makeText(this@CoursesActivity,board,Toast.LENGTH_SHORT).show()
                    board = ""
                    classList = mutableListOf()
                    binding.classSpinnerLay.gone()

                    binding.selectPackageHtv.gone()
                    binding.selectPackageLay.gone()
                    binding.streamSpinnerLay.gone()
                    binding.selectSubjectHtv.gone()
                    binding.subjectRecycler.gone()
                    //setupClassSpinner()
                    subjectList = mutableListOf()
                    binding.noDataLot.gone()
                    binding.noSubjectTv.gone()
                    binding.priceTv.text = "₹0"
                    boardId = 0
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

            }
        }
    }

    private fun getProfile(
        token: String
    ){
        binding.cartItemCountTv.gone()
        mGetProfileViewModel.getProfile(token).observe(this) { outcome ->
            when (outcome) {
                is Outcome.Success -> {
                    if (outcome.data.status == 1) {
                            if(outcome.data.result.result.cart_total_count != 0){
                                binding.cartItemCountTv.visible()
                                binding.cartItemCountTv.text = outcome.data.result.result.cart_total_count.toString()
                            }else{
                                binding.cartItemCountTv.gone()
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
            adapter = AddOnsListAdapter(list.toMutableList(),this@CoursesActivity, this@CoursesActivity)
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