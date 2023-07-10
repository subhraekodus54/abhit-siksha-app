package com.example.abithshiksha.view.fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.abithshiksha.R
import com.example.abithshiksha.databinding.FragmentCoursesBinding
import com.example.abithshiksha.helper.CourseSelectListener
import com.example.abithshiksha.helper.PrefManager
import com.example.abithshiksha.model.pojo.get_filtered_subject.Subject
import com.example.abithshiksha.model.repo.Outcome
import com.example.abithshiksha.view.activity.CartActivity
import com.example.abithshiksha.view.activity.CartDetailsActivity
import com.example.abithshiksha.view.adapter.SelectSubjectAdapter
import com.example.abithshiksha.view_model.AddToCartViewModel
import com.example.abithshiksha.view_model.GetBoardsViewModel
import com.example.abithshiksha.view_model.GetClassListViewModel
import com.example.abithshiksha.view_model.GetFilteredSubjectViewModel
import com.user.caregiver.gone
import com.user.caregiver.isConnectedToInternet
import com.user.caregiver.loadingDialog
import com.user.caregiver.visible
import org.koin.androidx.viewmodel.ext.android.viewModel

class CoursesFragment : Fragment(),CourseSelectListener {
    private var _binding: FragmentCoursesBinding? = null
    private val binding get() = _binding!!
    var classList: MutableList<String> = mutableListOf()
    val streamList: Array<String> = arrayOf("Select Stream", "Arts", "Commerce", "Science")
    var subjectList: MutableList<Int> = mutableListOf()
    var boardList: MutableList<String> =  mutableListOf()


    private var class_global: String = ""
    private var std: Int = 0
    private var board = ""
    private var stream = ""
    private var subject_price: Int = 0
    private var subject_count: Int = 0
    private var course_type: Int = 0
    private val mGetFilteredSubjectViewModel: GetFilteredSubjectViewModel by viewModel()
    private val mAddToCartViewModel: AddToCartViewModel by viewModel()
    private val mGetClassListViewModel: GetClassListViewModel by viewModel()
    private val mGetBoardsViewModel: GetBoardsViewModel by viewModel()

    lateinit var adapter: SelectSubjectAdapter
    private lateinit var accessToken: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCoursesBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //get token
        accessToken = "Bearer " + PrefManager.getKeyAuthToken()

        binding.priceTv.text = "$${subject_price.toString()}"
        binding.priceTv.text = "${subject_count.toString()}"

        binding.backArrow.gone()
        binding.courseHtv.gone()
        binding.classSpinnerLay.gone()
        binding.selectPackageLay.gone()
        binding.selectPackageHtv.gone()
        binding.selectSubjectHtv.gone()
        binding.subjectRecycler.gone()
        binding.streamSpinnerLay.gone()
        binding.subjectShimmerView.gone()
        binding.progressBar.gone()
        binding.noDataLot.gone()
        binding.noSubjectTv.gone()

        binding.countTv.setOnClickListener {
            /*if (subjectList.size > 0) {
                addToCart(course_type, subjectList, accessToken, 1)
            } else {
                Toast.makeText(requireActivity(), "please select a course.", Toast.LENGTH_SHORT)
                    .show()
            }*/

            if(board.isNotEmpty()){
                if(std != 0){
                    if(course_type != 0){
                        if(subjectList.size > 0){
                            addToCart(course_type, subjectList, accessToken, 1)
                        }else{
                            Toast.makeText(requireActivity(),"please select a subject.",Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        Toast.makeText(requireActivity(),"please select a package.",Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(requireActivity(),"please select a class.",Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(requireActivity(),"please select a board.",Toast.LENGTH_SHORT).show()
            }
        }

        binding.addToCardBtn.setOnClickListener {
            if(board.isNotEmpty()){
                if(std != 0){
                    if(course_type != 0){
                        if(subjectList.size > 0){
                            if(requireActivity().isConnectedToInternet()){
                                addToCart(course_type,subjectList,accessToken,0)
                            }else{
                                Toast.makeText(requireActivity(),"No internet connection.", Toast.LENGTH_LONG).show()
                            }
                        }else{
                            Toast.makeText(requireActivity(),"please select a subject.",Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        Toast.makeText(requireActivity(),"please select a package.",Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(requireActivity(),"please select a class.",Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(requireActivity(),"please select a board.",Toast.LENGTH_SHORT).show()
            }
        }

        //class spinner
        setupClassSpinner()

        //stream spinner
        setupStreamSpinner()

        /*binding.sebaLay.setOnClickListener {
            board = "SEBA"
            binding.sebaLay.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.btn_bg_1)
            binding.cbseLay.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.course_bg_one)
            binding.icscLay.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.course_bg_one)
            binding.sebaTv.setTextColor(Color.parseColor("#FFFFFF"))
            binding.cbseTv.setTextColor(Color.parseColor("#000000"))
            binding.icseTv.setTextColor(Color.parseColor("#000000"))
            //binding.classSpinnerLay.visible()
            classList = mutableListOf()
            getClassList(accessToken,board)

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
        }

        binding.cbseLay.setOnClickListener {
            board = "CBSE"
            binding.cbseLay.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.btn_bg_1)
            binding.sebaLay.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.course_bg_one)
            binding.icscLay.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.course_bg_one)
            binding.cbseTv.setTextColor(Color.parseColor("#FFFFFF"))
            binding.sebaTv.setTextColor(Color.parseColor("#000000"))
            binding.icseTv.setTextColor(Color.parseColor("#000000"))
            //binding.classSpinnerLay.visible()
            classList = mutableListOf()
            getClassList(accessToken,board)

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
        }

        binding.icscLay.setOnClickListener {
            board = "ICSE"
            binding.icscLay.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.btn_bg_1)
            binding.sebaLay.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.course_bg_one)
            binding.cbseLay.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.course_bg_one)
            binding.icseTv.setTextColor(Color.parseColor("#FFFFFF"))
            binding.cbseTv.setTextColor(Color.parseColor("#000000"))
            binding.sebaTv.setTextColor(Color.parseColor("#000000"))
            //binding.classSpinnerLay.visible()
            classList = mutableListOf()
            getClassList(accessToken,board)

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
        }*/

        //full package
        binding.fullLay.setOnClickListener {
            binding.fullLay.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.btn_bg_1)
            binding.customLay.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.course_bg_one)
            binding.fullTv.setTextColor(Color.parseColor("#FFFFFF"))
            binding.customTv.setTextColor(Color.parseColor("#000000"))
            binding.selectSubjectHtv.visible()
            binding.subjectShimmerView.visible()
            adapter = SelectSubjectAdapter(mutableListOf(), requireActivity(), false, this,0,false)
            binding.subjectRecycler.adapter = adapter
            subjectList = mutableListOf()
            course_type = 1
            binding.noDataLot.gone()
            binding.noSubjectTv.gone()

            getFilteredSubject(board, class_global, stream, accessToken, false)
        }

        //custom package
        binding.customLay.setOnClickListener {
            binding.customLay.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.btn_bg_1)
            binding.fullLay.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.course_bg_one)
            binding.customTv.setTextColor(Color.parseColor("#FFFFFF"))
            binding.fullTv.setTextColor(Color.parseColor("#000000"))
            binding.selectSubjectHtv.visible()
            binding.subjectShimmerView.visible()
            adapter = SelectSubjectAdapter(mutableListOf(), requireActivity(), true, this,0, false)
            binding.subjectRecycler.adapter = adapter
            subjectList = mutableListOf()
            course_type = 2
            binding.noDataLot.gone()
            binding.noSubjectTv.gone()

            getFilteredSubject(board, class_global, stream, accessToken, true)
        }

        getBoards()
    }

    private fun setupStreamSpinner() {
        val arrayAdapter2 = ArrayAdapter(
            requireActivity(),
            android.R.layout.simple_spinner_dropdown_item,
            streamList
        )
        binding.streamSpinner.adapter = arrayAdapter2
        binding.streamSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener,
            AdapterView.OnItemClickListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                stream = streamList[p2]

                //
                adapter = SelectSubjectAdapter(
                    mutableListOf(),
                    requireActivity(),
                    false,
                    this@CoursesFragment,
                    0,
                    false
                )
                binding.subjectRecycler.adapter = adapter
                binding.selectSubjectHtv.gone()
                binding.customLay.background =
                    ContextCompat.getDrawable(requireActivity(), R.drawable.course_bg_one)
                binding.fullLay.background =
                    ContextCompat.getDrawable(requireActivity(), R.drawable.course_bg_one)
                binding.customTv.setTextColor(Color.parseColor("#000000"))
                binding.fullTv.setTextColor(Color.parseColor("#000000"))
                subject_count = 0
                subject_price = 0
                binding.priceTv.text = "₹" + subject_price.toString()
                //binding.countTv.text = subject_count.toString()

                if (p2 > 0) {
                    binding.selectPackageHtv.visible()
                    binding.selectPackageLay.visible()
                    binding.subjectShimmerView.gone()
                    subjectList = mutableListOf()
                    binding.noDataLot.gone()
                    binding.noSubjectTv.gone()

                } else {
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
        val arrayAdapter = ArrayAdapter(
            requireActivity(),
            android.R.layout.simple_spinner_dropdown_item,
            classList
        )
        binding.classSpinner.adapter = arrayAdapter
        binding.classSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener,
            AdapterView.OnItemClickListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                class_global = classList[p2]
                std = p2
                course_type = 0
                //
                adapter = SelectSubjectAdapter(
                    mutableListOf(),
                    requireActivity(),
                    false,
                    this@CoursesFragment,
                    0,
                    false
                )
                binding.subjectRecycler.adapter = adapter
                binding.selectSubjectHtv.gone()
                binding.customLay.background =
                    ContextCompat.getDrawable(requireActivity(), R.drawable.course_bg_one)
                binding.fullLay.background =
                    ContextCompat.getDrawable(requireActivity(), R.drawable.course_bg_one)
                binding.customTv.setTextColor(Color.parseColor("#000000"))
                binding.fullTv.setTextColor(Color.parseColor("#000000"))
                subject_count = 0
                subject_price = 0
                binding.priceTv.text = "₹" + subject_price.toString()
                //binding.countTv.text = subject_count.toString()

                if (p2 > 0) {
                    if (p2 > 10) {
                        setupStreamSpinner()
                        binding.streamSpinnerLay.visible()
                        binding.selectPackageHtv.gone()
                        binding.selectPackageLay.gone()
                        binding.subjectShimmerView.gone()
                        subjectList = mutableListOf()
                        binding.noDataLot.gone()
                        binding.noSubjectTv.gone()

                    } else {
                        binding.selectPackageHtv.visible()
                        binding.selectPackageLay.visible()
                        binding.streamSpinnerLay.gone()
                        binding.subjectShimmerView.gone()
                        subjectList = mutableListOf()
                        binding.noDataLot.gone()
                        binding.noSubjectTv.gone()

                    }
                } else {
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
        val gridLayoutManager = GridLayoutManager(requireActivity(), 2)
        binding.subjectRecycler.apply {
            layoutManager = gridLayoutManager
            setHasFixedSize(true)
            isFocusable = false
            adapter = SelectSubjectAdapter(list, requireActivity(), isCustom, this@CoursesFragment,0,false)
        }
    }

    override fun onSelect(view: View, price: Int, isSelected: Boolean, id: Int) {
        if (isSelected) {
            subject_price += price
            subject_count += 1
            subjectList.add(id)
        } else {
            subject_price -= price
            subject_count -= 1
            subjectList.remove(id)
        }
        binding.priceTv.text = "₹${subject_price.toString()}"
        //binding.countTv.text = "${subject_count.toString()}"
    }

    private fun getFilteredSubject(
        board: String,
        standard: String,
        stream: String,
        token: String,
        isCustom: Boolean
    ) {
        binding.subjectShimmerView.visible()
        binding.subjectShimmerView.startShimmer()
        mGetFilteredSubjectViewModel.getFilteredSubject(board, standard, stream, token)
            .observe(requireActivity()) { outcome ->
                when (outcome) {
                    is Outcome.Success -> {
                        if (outcome.data.status == 1) {
                            binding.subjectShimmerView.stopShimmer()
                            binding.subjectShimmerView.gone()
                            if (isCustom == false) {
                                subject_count = outcome.data.result.result.subjects.size
                                subject_price = outcome.data.result.result.total_amount
                                binding.priceTv.text = "₹" + subject_price.toString()
                                //binding.countTv.text = subject_count.toString()
                                for (i in outcome.data.result.result.subjects) {
                                    subjectList.add(i.id)
                                }
                            } else {
                                subject_count = 0
                                subject_price = 0
                                binding.priceTv.text = "₹" + subject_price.toString()
                                //binding.countTv.text = subject_count.toString()
                                subjectList = mutableListOf()
                            }

                            if(outcome.data.result.result.subjects.isNotEmpty()){
                                binding.noDataLot.gone()
                                binding.noSubjectTv.gone()
                                binding.subjectRecycler.visible()
                                fillSuggestionRecyclerView(outcome.data.result.result.subjects, isCustom)
                            }else{
                                binding.noDataLot.visible()
                                binding.noSubjectTv.visible()
                                binding.subjectRecycler.gone()
                            }

                        } else {
                            Toast.makeText(
                                requireActivity(),
                                outcome.data.result.message,
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    }
                    is Outcome.Failure<*> -> {
                        Toast.makeText(requireActivity(), R.string.network_issue, Toast.LENGTH_SHORT).show()

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
        isBuy: Int
    ) {
        val loader = requireActivity().loadingDialog()
        loader.show()
        mAddToCartViewModel.addToCart(course_type, subjects, isBuy, token)
            .observe(requireActivity()) { outcome ->
                loader.dismiss()
                when (outcome) {
                    is Outcome.Success -> {
                        if (outcome.data.status == 1) {
                            if (isBuy == 1) {

                                val intent = Intent(requireActivity(), CartDetailsActivity::class.java)
                                intent.putExtra("id", outcome.data.result.cart_id)
                                startActivity(intent)

                            } else {
                                val intent = Intent(requireActivity(), CartActivity::class.java)
                                startActivity(intent)
                            }

                        } else {
                            Toast.makeText(
                                requireActivity(),
                                outcome.data.result.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    is Outcome.Failure<*> -> {
                        Toast.makeText(requireActivity(), R.string.network_issue, Toast.LENGTH_SHORT).show()

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
        mGetClassListViewModel.getClassList(accessToken,board_name).observe(requireActivity()) { outcome ->
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
                        Toast.makeText(requireActivity(), outcome.data.result.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                is Outcome.Failure<*> -> {
                    Toast.makeText(requireActivity(), R.string.network_issue, Toast.LENGTH_SHORT).show()

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
        mGetBoardsViewModel.getBoards().observe(viewLifecycleOwner) { outcome ->
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
                            setupBoardSpinner(boardList)
                        }else{
                            binding.boardSpinner.gone()
                            binding.noBoardTv.visible()
                        }


                    } else {
                        Toast.makeText(requireActivity(), outcome.data.result.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                is Outcome.Failure<*> -> {
                    Toast.makeText(requireActivity(), R.string.network_issue, Toast.LENGTH_SHORT).show()

                    Log.i("statusMsg", outcome.e.message.toString())

                    outcome.e.printStackTrace()
                    Log.i("status", outcome.e.cause.toString())
                }
            }
        }
    }

    private fun setupBoardSpinner(boardList: List<String>) {
        val arrayAdapter2 = ArrayAdapter(requireActivity(),android.R.layout.simple_spinner_dropdown_item,boardList)
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
                }else{
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
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

            }
        }
    }

}