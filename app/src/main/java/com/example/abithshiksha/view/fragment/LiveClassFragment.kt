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
import com.example.abithshiksha.databinding.FragmentGalleryBinding
import com.example.abithshiksha.databinding.FragmentLiveClassBinding
import com.example.abithshiksha.helper.PrefManager
import com.example.abithshiksha.model.pojo.TestModel
import com.example.abithshiksha.model.pojo.get_time_table.ResultX
import com.example.abithshiksha.model.repo.Outcome
import com.example.abithshiksha.view.adapter.LiveClassAdapter
import com.example.abithshiksha.view.adapter.PurchaseHistoryListAdapter
import com.example.abithshiksha.view_model.GetGalleryViewModel
import com.example.abithshiksha.view_model.GetTimeTableViewModel
import com.user.caregiver.gone
import com.user.caregiver.isConnectedToInternet
import com.user.caregiver.visible
import org.koin.androidx.viewmodel.ext.android.viewModel

class LiveClassFragment : Fragment() {
    private var _binding: FragmentLiveClassBinding? = null
    private val binding get() = _binding!!

    private val mGetTimeTableViewModel: GetTimeTableViewModel by viewModel()
    private lateinit var accessToken: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLiveClassBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //get token
        accessToken = "Bearer "+PrefManager.getKeyAuthToken()

        binding.liveClassRecycler.gone()
        binding.liveClassShimmerView.startShimmer()
        binding.emptyListLot.gone()
        binding.noDataHtv.gone()
        if(requireActivity().isConnectedToInternet()){
            getTimeTable(accessToken)
        }else{
            Toast.makeText(requireActivity(),"No internet connection.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fillRecycler(list: List<ResultX>) {
        val gridLayoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        binding.liveClassRecycler.apply {
            layoutManager = gridLayoutManager
            adapter = LiveClassAdapter(list,requireActivity())
        }
    }

    private fun getTimeTable(
        token: String
    ){
        binding.liveClassRecycler.gone()
        binding.liveClassShimmerView.startShimmer()
        binding.emptyListLot.gone()
        binding.noDataHtv.gone()
        mGetTimeTableViewModel.getTimeTable(token).observe(viewLifecycleOwner) { outcome ->
            when (outcome) {
                is Outcome.Success -> {
                    if (outcome.data.status == 1) {
                        binding.liveClassShimmerView.stopShimmer()
                        binding.liveClassShimmerView.gone()

                        if(outcome.data.result.result != null && outcome.data.result.result.size>0){
                            binding.emptyListLot.gone()
                            binding.noDataHtv.gone()
                            binding.liveClassRecycler.visible()
                            fillRecycler(outcome.data.result.result)
                        }else{
                            binding.liveClassRecycler.gone()
                            binding.emptyListLot.visible()
                            binding.noDataHtv.visible()
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