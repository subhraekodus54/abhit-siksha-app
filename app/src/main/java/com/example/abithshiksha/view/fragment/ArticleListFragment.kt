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
import com.example.abithshiksha.databinding.FragmentArticleListBinding
import com.example.abithshiksha.databinding.FragmentPdfListBinding
import com.example.abithshiksha.databinding.FragmentVideoListBinding
import com.example.abithshiksha.helper.PaginationScrollListener
import com.example.abithshiksha.helper.PrefManager
import com.example.abithshiksha.model.pojo.get_article.Content
import com.example.abithshiksha.model.pojo.get_videos.Video
import com.example.abithshiksha.model.repo.Outcome
import com.example.abithshiksha.view.adapter.ArticleListAdapter
import com.example.abithshiksha.view.adapter.VideoListAdapter
import com.example.abithshiksha.view_model.GetArticleViewModel
import com.example.abithshiksha.view_model.GetVideosViewModel
import com.user.caregiver.gone
import com.user.caregiver.isConnectedToInternet
import com.user.caregiver.visible
import org.koin.androidx.viewmodel.ext.android.viewModel

class ArticleListFragment(private val topic_id: Int) : Fragment() {
    private var _binding: FragmentArticleListBinding? = null
    private val binding get() = _binding!!
    private val mGetArticleViewModel: GetArticleViewModel by viewModel()
    private lateinit var accessToken: String

    var isLastPage: Boolean = false
    var isLoading: Boolean = false
    var page_no = 1
    lateinit var adapter: ArticleListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentArticleListBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //get token
        accessToken = "Bearer "+ PrefManager.getKeyAuthToken()

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.articleRecycler.layoutManager = layoutManager
        binding.articleRecycler?.addOnScrollListener(object : PaginationScrollListener(layoutManager) {
            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }

            override fun loadMoreItems() {
                isLoading = true
                page_no++
                getArticle(accessToken,topic_id,page_no)
            }
        })

    }

    override fun onStart() {
        super.onStart()
        //recycle view pagination
        binding.articleRecycler.gone()
        binding.articleShimmerView.startShimmer()
        binding.emptyListLot.gone()
        binding.noDataHtv.gone()
        adapter = ArticleListAdapter(mutableListOf(),requireActivity())
        binding.articleRecycler.adapter = adapter
        if(requireActivity().isConnectedToInternet()){
            page_no = 1
            getArticle(accessToken,topic_id,page_no)
        }else{
            Toast.makeText(requireActivity(),"No internet connection.", Toast.LENGTH_LONG).show()
        }
    }

    private fun getArticle(
        token: String,
        topic_id: Int,
        page: Int
    ){
        mGetArticleViewModel.getArticle(token, topic_id, page).observe(viewLifecycleOwner) { outcome ->
            when (outcome) {
                is Outcome.Success -> {
                    if (outcome.data.status == 1) {
                        binding.articleShimmerView.stopShimmer()
                        binding.articleShimmerView.gone()
                        if(outcome.data.result.result.contents.isNotEmpty()){
                            binding.articleRecycler.visible()
                            binding.emptyListLot.gone()
                            binding.noDataHtv.gone()

                            outcome.data.result.result.contents?.let {
                                adapter.add(it)
                                isLoading = false
                            }
                        }else{
                            if (page_no==1){
                                binding.articleRecycler.gone()
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
}