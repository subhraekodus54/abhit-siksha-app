package com.example.abithshiksha.view.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.models.SlideModel
import com.example.abithshiksha.R
import com.example.abithshiksha.databinding.FragmentPdfListBinding
import com.example.abithshiksha.databinding.FragmentVideoListBinding
import com.example.abithshiksha.helper.PaginationScrollListener
import com.example.abithshiksha.helper.PrefManager
import com.example.abithshiksha.helper.click_listener.VideoClickListener
import com.example.abithshiksha.model.network.ApiConstants
import com.example.abithshiksha.model.pojo.get_topics.ResultX
import com.example.abithshiksha.model.pojo.get_videos.Video
import com.example.abithshiksha.model.repo.Outcome
import com.example.abithshiksha.view.activity.VideoPlayActivity
import com.example.abithshiksha.view.adapter.LessonsListAdapter
import com.example.abithshiksha.view.adapter.TopicListAdapter
import com.example.abithshiksha.view.adapter.VideoListAdapter
import com.example.abithshiksha.view_model.GetBannerViewModel
import com.example.abithshiksha.view_model.GetVideosViewModel
import com.user.caregiver.gone
import com.user.caregiver.isConnectedToInternet
import com.user.caregiver.visible
import org.koin.androidx.viewmodel.ext.android.viewModel

class VideoListFragment(private val topic_id: Int) : Fragment(), VideoClickListener {
    private var _binding: FragmentVideoListBinding? = null
    private val binding get() = _binding!!
    private val mGetVideosViewModel: GetVideosViewModel by viewModel()
    private lateinit var accessToken: String
    private lateinit var user_id: String

    var isLastPage: Boolean = false
    var isLoading: Boolean = false
    var page_no = 1
    lateinit var adapter: VideoListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVideoListBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //get token
        accessToken = "Bearer "+ PrefManager.getKeyAuthToken()
        binding.emptyListLot.gone()
        binding.noDataHtv.gone()

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.videoRecycler.layoutManager = layoutManager
        binding.videoRecycler?.addOnScrollListener(object : PaginationScrollListener(layoutManager) {
            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }

            override fun loadMoreItems() {
                isLoading = true
                page_no++
                getVideos(accessToken,topic_id,page_no)
            }
        })

    }

    override fun onStart() {
        super.onStart()
        //recycle view pagination
        binding.videoRecycler.gone()
        binding.videoShimmerView.startShimmer()
        binding.noDataHtv.gone()
        binding.emptyListLot.gone()
        adapter = VideoListAdapter(mutableListOf(),requireActivity(), this)
        binding.videoRecycler.adapter = adapter
        if(requireActivity().isConnectedToInternet()){
            page_no = 1
            getVideos(accessToken,topic_id,page_no)
        }else{
            Toast.makeText(requireActivity(),"No internet connection.", Toast.LENGTH_LONG).show()
        }
    }

    private fun getVideos(
        token: String,
        topic_id: Int,
        page: Int
    ){
        mGetVideosViewModel.getVideos(token, topic_id, page).observe(viewLifecycleOwner) { outcome ->
            when (outcome) {
                is Outcome.Success -> {
                    if (outcome.data.status == 1) {
                        binding.videoShimmerView.stopShimmer()
                        binding.videoShimmerView.gone()
                        if(outcome.data.result.result.videos.isNotEmpty()){
                            binding.videoRecycler.visible()
                            binding.emptyListLot.gone()
                            binding.noDataHtv.gone()

                            outcome.data.result.result.videos?.let {
                                adapter.add(it)
                                isLoading = false
                            }

                            outcome.data.result.result.user_id?.let {
                                user_id = it.toString()
                            }
                        }else{
                            if (page_no==1){
                                binding.videoRecycler.gone()
                                binding.emptyListLot.visible()
                                binding.noDataHtv.visible()

                            }
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

    override fun onClick(view: View, id: Int, url: String) {
        val intent = Intent(context, VideoPlayActivity::class.java)
        intent.putExtra("url",url)
        intent.putExtra("id",id)
        intent.putExtra("user_id", user_id)
        startActivity(intent)
    }

}