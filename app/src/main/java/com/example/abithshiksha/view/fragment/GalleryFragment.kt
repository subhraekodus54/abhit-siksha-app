package com.example.abithshiksha.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.example.abithshiksha.R
import com.example.abithshiksha.databinding.FragmentGalleryBinding
import com.example.abithshiksha.helper.PrefManager
import com.example.abithshiksha.model.pojo.get_gallery.ResultX
import com.example.abithshiksha.model.repo.Outcome
import com.example.abithshiksha.view.adapter.GalleryAdapter
import com.example.abithshiksha.view_model.GetGalleryViewModel
import com.user.caregiver.gone
import com.user.caregiver.isConnectedToInternet
import com.user.caregiver.visible
import org.koin.androidx.viewmodel.ext.android.viewModel


class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!
    private val mGetGalleryViewModel: GetGalleryViewModel by viewModel()
    private lateinit var accessToken: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //get token
        accessToken = "Bearer "+ PrefManager.getKeyAuthToken()

        networkCall()
        binding.swipeRefreshLayout.setOnRefreshListener(object : OnRefreshListener {
            override fun onRefresh() {
                networkCall()
            }
        })
    }

    private fun networkCall(){
        if(requireActivity().isConnectedToInternet()){
            getGallery(accessToken)
        }else{
            Toast.makeText(requireActivity(),"No internet connection.",Toast.LENGTH_LONG).show()
        }
    }

    private fun fillGalleryRecyclerView(list: List<ResultX>) {
        val gridLayoutManager = GridLayoutManager(activity, 2)
        binding.galleryRecycler.apply {
            layoutManager = gridLayoutManager
            setHasFixedSize(true)
            isFocusable = false
            adapter = GalleryAdapter(list,requireActivity())
        }
    }

    private fun getGallery(
        token: String
    ){
        binding.galleryRecycler.gone()
        binding.gellaryShimmerView.startShimmer()
        binding.emptyListLot.gone()
        binding.noDataHtv.gone()
        mGetGalleryViewModel.getGallery(token).observe(viewLifecycleOwner) { outcome ->
            when (outcome) {
                is Outcome.Success -> {
                    if (outcome.data.status == 1) {
                        binding.swipeRefreshLayout.setRefreshing(false)
                        binding.gellaryShimmerView.stopShimmer()
                        binding.gellaryShimmerView.gone()

                        if(outcome.data.result.result != null && outcome.data.result.result.size>0){
                            binding.emptyListLot.gone()
                            binding.noDataHtv.gone()
                            binding.galleryRecycler.visible()
                            fillGalleryRecyclerView(outcome.data.result.result)
                        }else{
                            binding.galleryRecycler.gone()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}