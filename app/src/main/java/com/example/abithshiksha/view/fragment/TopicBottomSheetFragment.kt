package com.example.abithshiksha.view.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.abithshiksha.R
import com.example.abithshiksha.databinding.FragmentTopicBottomSheetBinding
import com.example.abithshiksha.helper.PrefManager
import com.example.abithshiksha.model.pojo.get_topic_list.ResultX
import com.example.abithshiksha.model.repo.Outcome
import com.example.abithshiksha.view.activity.AuthActivity
import com.example.abithshiksha.view.adapter.GalleryAdapter
import com.example.abithshiksha.view.adapter.TopicListAdapter2
import com.example.abithshiksha.view_model.GetTopicListViewModel
import com.example.abithshiksha.view_model.LogoutViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.user.caregiver.gone
import com.user.caregiver.isConnectedToInternet
import com.user.caregiver.loadingDialog
import com.user.caregiver.visible
import org.koin.androidx.viewmodel.ext.android.viewModel

class TopicBottomSheetFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentTopicBottomSheetBinding? = null
    private val binding get() = _binding!!

    private val mGetTopicListViewModel: GetTopicListViewModel by viewModel()
    private lateinit var accessToken: String
    private var lesson_id: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            lesson_id = it.getInt("lesson_id")!!
            //Toast.makeText(requireActivity(),lesson_id.toString(),Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTopicBottomSheetBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //get token
        accessToken = "Bearer "+ PrefManager.getKeyAuthToken()

        if(requireActivity().isConnectedToInternet()){
            getTopicList(accessToken,lesson_id)
        }else{
            Toast.makeText(requireActivity(),"No internet connection.",Toast.LENGTH_SHORT).show()
        }
    }

    private fun fillTopicListRecyclerView(list: List<ResultX>) {
        val gridLayoutManager = LinearLayoutManager(activity)
        binding.topicRecycler.apply {
            layoutManager = gridLayoutManager
            setHasFixedSize(true)
            isFocusable = false
            adapter = TopicListAdapter2(list,requireActivity())
        }
    }

    private fun getTopicList(
        token: String,
        lesson_id: Int
    ){
        binding.topicRecycler.gone()
        binding.topicListShimmerView.startShimmer()
        binding.topicListShimmerView.visible()
        binding.noDataImg.gone()
        binding.noDataHtv.gone()
        mGetTopicListViewModel.getTopicList(token,lesson_id).observe(viewLifecycleOwner) { outcome ->
            when (outcome) {
                is Outcome.Success -> {
                    if (outcome.data.status == 1) {
                        if(outcome.data.result.result.size>0 && outcome.data.result.result!=null){
                            binding.topicRecycler.visible()
                            binding.topicListShimmerView.stopShimmer()
                            binding.topicListShimmerView.gone()
                            binding.noDataImg.gone()
                            binding.noDataHtv.gone()
                            fillTopicListRecyclerView(outcome.data.result.result)
                        }else{
                            binding.topicRecycler.gone()
                            binding.noDataImg.visible()
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