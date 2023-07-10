package com.example.abithshiksha.view.fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.abithshiksha.R
import com.example.abithshiksha.databinding.FragmentProgressBinding
import com.example.abithshiksha.helper.PrefManager
import com.example.abithshiksha.model.repo.Outcome
import com.example.abithshiksha.view_model.GetAllPerformanceViewModel
import com.example.abithshiksha.view_model.GetSubjectPerformanceViewModel
import com.user.caregiver.isConnectedToInternet
import com.user.caregiver.loadingDialog
import org.eazegraph.lib.models.BarModel
import org.eazegraph.lib.models.PieModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PerformanceFragment : Fragment() {
    private var _binding: FragmentProgressBinding? = null
    private val binding get() = _binding!!
    private val mGetAllPerformanceViewModel: GetAllPerformanceViewModel by viewModel()
    private val mGetSubjectPerformanceViewModel: GetSubjectPerformanceViewModel by viewModel()

    private lateinit var accessToken: String

    var subjectList: MutableList<String> =  mutableListOf()
    var subjectIdList: MutableList<Int> =  mutableListOf()
    private var subject = "All"
    private var subjectId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProgressBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //get token
        accessToken = "Bearer "+ PrefManager.getKeyAuthToken()

        if(requireActivity().isConnectedToInternet()){
            getSubjectList(accessToken)
        }else{
            Toast.makeText(requireActivity(),"No internet connection.",Toast.LENGTH_SHORT).show()
        }

    }

    private fun setupSubjectSpinner(subjectList: List<String>,subjectIdList: List<Int>) {
        val arrayAdapter2 = ArrayAdapter(requireActivity(),android.R.layout.simple_spinner_dropdown_item,subjectList)
        binding.subjectSpinner.adapter = arrayAdapter2
        binding.subjectSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener,
            AdapterView.OnItemClickListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                subject = subjectList[p2]
                subjectId = subjectIdList[p2]
                if(p2 != 0){
                    if(requireActivity().isConnectedToInternet()){
                        getSubjectPerformance(accessToken,subjectId)
                    }else{
                        Toast.makeText(requireActivity(),"No Internet Connection.",Toast.LENGTH_SHORT).show()
                    }
                }else{
                    getAllClickPerformance(accessToken)
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

            }
        }
    }

    private fun showChart(
        watched: Int,
        not_watched: Int
    ){

        //correct
        binding.pieChart.addPieSlice(
            PieModel(
                "watched", watched.toFloat(),
                Color.parseColor("#0CB300")
            )
        )

        //incorrect
        binding.pieChart.addPieSlice(
            PieModel(
                "not watched", not_watched.toFloat(),
                Color.parseColor("#C1C1C1")
            )
        )
        binding.pieChart.startAnimation()
    }

    private fun showLineChart(mon: Int, tue: Int, wed: Int, thu: Int, fri: Int, sat: Int, sun: Int){
        binding.barchart.addBar(BarModel("Mon",mon.toFloat(), -0xedcbaa))
        binding.barchart.addBar(BarModel("Tue",tue.toFloat(), -0xcbcbaa))
        binding.barchart.addBar(BarModel("Wed",wed.toFloat(), -0xa9cbaa))
        binding.barchart.addBar(BarModel("Thu",thu.toFloat(), -0x78c0aa))
        binding.barchart.addBar(BarModel("Fri",fri.toFloat(), -0xa9480f))
        binding.barchart.addBar(BarModel("Sat",sat.toFloat(), -0xcbcbaa))
        binding.barchart.addBar(BarModel("Sun",sun.toFloat(), -0xe00b54))

        binding.barchart.startAnimation()
    }

    private fun getSubjectList(
        token: String
    ) {
        subjectList = mutableListOf()
        subjectIdList = mutableListOf()
        val loader = requireActivity().loadingDialog(false,R.raw.no_subject)
        loader.show()
        mGetAllPerformanceViewModel.getAllPerformance(token,)
            .observe(requireActivity()) { outcome ->
                loader.dismiss()
                when (outcome) {
                    is Outcome.Success -> {
                        if (outcome.data.status == 1) {
                            subjectList.add("All")
                            subjectIdList.add(0)

                            for (i in outcome.data.result.all_subjects){
                                subjectList.add(i.name)
                            }
                            for (i in outcome.data.result.all_subjects){
                                subjectIdList.add(i.id)
                            }
                            setupSubjectSpinner(subjectList,subjectIdList)

                        } else {
                            Toast.makeText(
                                requireActivity(),
                                "Something went wrong",
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

    private fun getSubjectPerformance(
        token: String,
        id: Int
    ) {
        val loader = requireActivity().loadingDialog(false,R.raw.no_subject)
        loader.show()
        mGetSubjectPerformanceViewModel.getSubjectDetails(token,id)
            .observe(requireActivity()) { outcome ->
                loader.dismiss()
                when (outcome) {
                    is Outcome.Success -> {
                        if (outcome.data.status == 1) {

                            //piechart
                            if(outcome.data.result.subject_progress.watched_percentage != null
                                && outcome.data.result.subject_progress.not_watched_percentage != null){
                                binding.pieChart.clearChart()
                                showChart(outcome.data.result.subject_progress.watched_percentage, outcome.data.result.subject_progress.not_watched_percentage)
                            }

                            //subject progress
                            outcome.data.result.subject_progress.subject_progress?.let {
                                binding.circularProgress.setProgress(
                                    it.toDouble(),
                                    100.00
                                )
                            }

                            //Line Chart
                            binding.barchart.clearChart()
                            outcome.data.result.time_spent?.let {
                                showLineChart(
                                    outcome.data.result.time_spent.Mon,
                                    outcome.data.result.time_spent.Tue,
                                    outcome.data.result.time_spent.Wed,
                                    outcome.data.result.time_spent.Thu,
                                    outcome.data.result.time_spent.Fri,
                                    outcome.data.result.time_spent.Sat,
                                    outcome.data.result.time_spent.Sun,
                                )
                            }

                            //test performance
                            outcome.data.result.mcq_performance.test_attempted?.let {
                                binding.attemptedTv.text = it.toString()
                            }
                            outcome.data.result.mcq_performance.total_correct?.let { correct ->
                                outcome.data.result.mcq_performance.total_attempted?.let { total ->
                                    binding.correctATv.text = correct.toString()+"/"+total.toString()
                                }
                            }
                            outcome.data.result.mcq_performance.accuracy?.let {
                                binding.accuracyTv.text = it.toString()
                            }
                            outcome.data.result.mcq_performance.total_duration?.let {
                                binding.totalTimeTv.text = it.toString()
                            }
                            outcome.data.result.subject_progress.subject_progress?.let {
                                binding.progressTv.text = it.toString()+"%"
                            }

                        } else {
                            Toast.makeText(
                                requireActivity(),
                                "Something went wrong",
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

    private fun getAllClickPerformance(
        token: String
    ) {
        subjectList = mutableListOf()
        subjectIdList = mutableListOf()
        val loader = requireActivity().loadingDialog(false,R.raw.no_subject)
        loader.show()
        mGetAllPerformanceViewModel.getAllPerformance(token,)
            .observe(requireActivity()) { outcome ->
                loader.dismiss()
                when (outcome) {
                    is Outcome.Success -> {
                        if (outcome.data.status == 1) {
                            //piechart
                            if(outcome.data.result.subject_progress.watched_percentage != null
                                && outcome.data.result.subject_progress.not_watched_percentage != null){
                                binding.pieChart.clearChart()
                                showChart(outcome.data.result.subject_progress.watched_percentage, outcome.data.result.subject_progress.not_watched_percentage)
                            }

                            //subject progress
                            outcome.data.result.subject_progress.subject_progress?.let {
                                binding.circularProgress.setProgress(
                                    it.toDouble(),
                                    100.00
                                )
                            }

                            //Line Chart
                            binding.barchart.clearChart()
                            outcome.data.result.time_spent?.let {
                                showLineChart(
                                    outcome.data.result.time_spent.Mon,
                                    outcome.data.result.time_spent.Tue,
                                    outcome.data.result.time_spent.Wed,
                                    outcome.data.result.time_spent.Thu,
                                    outcome.data.result.time_spent.Fri,
                                    outcome.data.result.time_spent.Sat,
                                    outcome.data.result.time_spent.Sun,
                                )
                            }

                            //test performance
                            outcome.data.result.mcq_performance.test_attempted?.let {
                                binding.attemptedTv.text = it.toString()
                            }
                            outcome.data.result.mcq_performance.total_correct?.let { correct ->
                                outcome.data.result.mcq_performance.total_attempted?.let { total ->
                                    binding.correctATv.text = correct.toString()+"/"+total.toString()
                                }
                            }
                            outcome.data.result.mcq_performance.accuracy?.let {
                                binding.accuracyTv.text = it.toString()
                            }
                            outcome.data.result.mcq_performance.total_duration?.let {
                                binding.totalTimeTv.text = it.toString()
                            }
                            outcome.data.result.subject_progress.subject_progress?.let {
                                binding.progressTv.text = it.toString()+"%"
                            }

                        } else {
                            Toast.makeText(
                                requireActivity(),
                                "Something went wrong",
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

}