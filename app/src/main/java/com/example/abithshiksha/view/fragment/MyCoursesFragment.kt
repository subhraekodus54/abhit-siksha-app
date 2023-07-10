package com.example.abithshiksha.view.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.abithshiksha.R
import com.example.abithshiksha.databinding.FragmentMyCoursesBinding
import com.example.abithshiksha.helper.PrefManager
import com.example.abithshiksha.model.pojo.get_orders.Course
import com.example.abithshiksha.model.repo.Outcome
import com.example.abithshiksha.view.adapter.MygCoursesAdapter
import com.example.abithshiksha.view_model.GetOrdersViewModel
import com.user.caregiver.gone
import com.user.caregiver.isConnectedToInternet
import com.user.caregiver.visible
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyCoursesFragment : Fragment() {

    private var _binding: FragmentMyCoursesBinding? = null
    private val binding get() = _binding!!
    private val mGetOrdersViewModel: GetOrdersViewModel by viewModel()

    private lateinit var accessToken: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyCoursesBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //get token
        accessToken = "Bearer "+ PrefManager.getKeyAuthToken()

        if(requireActivity().isConnectedToInternet()){
            getMyCourses(accessToken)
        }else{
            Toast.makeText(requireActivity(),"No internet connection.", Toast.LENGTH_LONG).show()
        }
    }

    private fun fillMyCoursesRecycler(list: List<Course>) {
        val gridLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.myCoursesRecycler.apply {
            layoutManager = gridLayoutManager
            adapter = MygCoursesAdapter(list,requireActivity())
        }
    }

    private fun getMyCourses(
        token: String
    ){
        binding.courseShimmerView.visible()
        binding.courseShimmerView.startShimmer()
        binding.myCoursesRecycler.gone()
        binding.noDataLot.gone()
        binding.noDataTv.gone()
        mGetOrdersViewModel.getOrder(token).observe(viewLifecycleOwner) { outcome ->
            when (outcome) {
                is Outcome.Success -> {
                    if (outcome.data.status == 1) {
                        binding.courseShimmerView.gone()
                        binding.courseShimmerView.stopShimmer()
                        if(outcome.data.result.courses.size != 0 && outcome.data.result.courses != null){
                            binding.myCoursesRecycler.visible()
                            binding.noDataLot.gone()
                            binding.noDataTv.gone()
                            fillMyCoursesRecycler(outcome.data.result.courses)
                        }else{
                            binding.noDataLot.visible()
                            binding.noDataTv.visible()
                            binding.myCoursesRecycler.gone()
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

}