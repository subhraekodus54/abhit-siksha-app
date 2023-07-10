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
import com.example.abithshiksha.databinding.FragmentLoginBinding
import com.example.abithshiksha.databinding.FragmentPdfListBinding
import com.example.abithshiksha.helper.PaginationScrollListener
import com.example.abithshiksha.helper.PrefManager
import com.example.abithshiksha.model.pojo.get_pdf.Pdf
import com.example.abithshiksha.model.pojo.get_videos.Video
import com.example.abithshiksha.model.repo.Outcome
import com.example.abithshiksha.view.adapter.PdfListAdapter
import com.example.abithshiksha.view.adapter.VideoListAdapter
import com.example.abithshiksha.view_model.GetPdfViewModel
import com.example.abithshiksha.view_model.GetVideosViewModel
import com.user.caregiver.gone
import com.user.caregiver.isConnectedToInternet
import com.user.caregiver.visible
import org.koin.androidx.viewmodel.ext.android.viewModel

class PdfListFragment(private val topic_id: Int) : Fragment() {
    private var _binding: FragmentPdfListBinding? = null
    private val binding get() = _binding!!
    private val mGetPdfViewModel: GetPdfViewModel by viewModel()
    private lateinit var accessToken: String

    var isLastPage: Boolean = false
    var isLoading: Boolean = false
    var page_no = 1
    lateinit var adapter: PdfListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPdfListBinding.inflate(inflater, container, false)
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
        binding.pdfRecycler.layoutManager = layoutManager
        binding.pdfRecycler?.addOnScrollListener(object : PaginationScrollListener(layoutManager) {
            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }

            override fun loadMoreItems() {
                isLoading = true
                page_no++
                getPdf(accessToken,topic_id,page_no)
            }
        })
    }

    override fun onStart() {
        super.onStart()
        //recycle view pagination
        binding.pdfRecycler.gone()
        binding.pdfShimmerView.startShimmer()
        binding.emptyListLot.gone()
        binding.noDataHtv.gone()

        adapter = PdfListAdapter(mutableListOf(),requireActivity())
        binding.pdfRecycler.adapter = adapter
        if(requireActivity().isConnectedToInternet()){
            page_no = 1
            getPdf(accessToken,topic_id,page_no)
        }else{
            Toast.makeText(requireActivity(),"No internet connection.", Toast.LENGTH_LONG).show()
        }
    }

    private fun fillPdfRecycler(list: List<Pdf>) {
        val linearLayoutManager = LinearLayoutManager(requireActivity())
        binding.pdfRecycler.apply {
            layoutManager = linearLayoutManager
            setHasFixedSize(true)
            isFocusable = false
            adapter = PdfListAdapter(list.toMutableList(),requireActivity())
        }
    }

    private fun getPdf(
        token: String,
        topic_id: Int,
        page: Int
    ){
        mGetPdfViewModel.getPdf(token, topic_id, page).observe(viewLifecycleOwner) { outcome ->
            when (outcome) {
                is Outcome.Success -> {
                    if (outcome.data.status == 1) {
                        binding.pdfShimmerView.stopShimmer()
                        binding.pdfShimmerView.gone()
                        if(outcome.data.result.result.pdfs.isNotEmpty()){
                            binding.pdfRecycler.visible()
                            binding.emptyListLot.gone()
                            binding.noDataHtv.gone()

                            outcome.data.result.result.pdfs?.let {
                                adapter.add(it)
                                isLoading = false
                            }
                        }else{
                            if (page_no==1){
                                binding.pdfRecycler.gone()
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