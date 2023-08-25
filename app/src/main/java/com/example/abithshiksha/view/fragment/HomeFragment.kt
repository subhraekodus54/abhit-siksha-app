package com.example.abithshiksha.view.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.denzcoskun.imageslider.models.SlideModel
import com.example.abithshiksha.R
import com.example.abithshiksha.databinding.FragmentHomeBinding
import com.example.abithshiksha.helper.PrefManager
import com.example.abithshiksha.helper.click_listener.SubjectClickListener
import com.example.abithshiksha.model.network.ApiConstants
import com.example.abithshiksha.model.pojo.upcomming_response.ResultX
import com.example.abithshiksha.model.repo.Outcome
import com.example.abithshiksha.view.activity.CourseDetailsActivity
import com.example.abithshiksha.view.activity.CoursesActivity
import com.example.abithshiksha.view.activity.WebViewActivity
import com.example.abithshiksha.view.adapter.SuggestionCourseAdapter
import com.example.abithshiksha.view.adapter.UpcomingCourseAdapter
import com.example.abithshiksha.view_model.GetBannerViewModel
import com.example.abithshiksha.view_model.GetSuggestionCoursesViewModel
import com.example.abithshiksha.view_model.UpcommingCoursesViewModel
import com.thekhaeng.pushdownanim.PushDownAnim
import com.user.caregiver.gone
import com.user.caregiver.isConnectedToInternet
import com.user.caregiver.visible
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment(), SubjectClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val mGetBannerViewModel: GetBannerViewModel by viewModel()
    private val mUpcommingCoursesViewModel: UpcommingCoursesViewModel by viewModel()
    private val mGetSuggestionCoursesViewModel: GetSuggestionCoursesViewModel by viewModel()
    private lateinit var accessToken: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //get token
        accessToken = "Bearer "+ PrefManager.getKeyAuthToken()

        //teacher img
        Glide.with(requireActivity())
            .load("https://images.unsplash.com/photo-1580894732444-8ecded7900cd?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MXx8dGVhY2hlcnxlbnwwfHwwfHw%3D&auto=format&fit=crop&w=500&q=60") // image url
            .placeholder(R.drawable.error_img_bg) // any placeholder to load at start
            .centerCrop()
            .into(binding.teacherImg)

        PushDownAnim.setPushDownAnimTo(binding.teacherBtn).setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse("http://abhithsiksha.com/become-a-teacher")
            startActivity(i)
        }

        PushDownAnim.setPushDownAnimTo(binding.suggestionViewAllBtn).setOnClickListener {
            val intent = Intent(requireActivity(), CoursesActivity::class.java)
            startActivity(intent)
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            networkCall()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun fillSuggestionRecyclerView(list: List<com.example.abithshiksha.model.pojo.suggestion_courses.ResultX>) {
        val gridLayoutManager = GridLayoutManager(activity, 2)
        binding.suggestionCourseRecycler.apply {
            layoutManager = gridLayoutManager
            setHasFixedSize(true)
            isFocusable = false
            adapter = SuggestionCourseAdapter(list,requireActivity(), this@HomeFragment)
        }
    }

    private fun fillUpcomingRecyclerView(list: List<ResultX>) {
        val gridLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        binding.upcomingCourseRecycler.apply {
            layoutManager = gridLayoutManager
            adapter = UpcomingCourseAdapter(list,requireActivity(),this@HomeFragment)
        }
    }

    override fun onStart() {
        networkCall()
        super.onStart()
    }

    private fun networkCall(){
        if(requireActivity().isConnectedToInternet()){
            getBanner(accessToken)
            getUpcommingCourses(accessToken)
            getSuggestionCourses(accessToken)
        }else{
            Toast.makeText(requireActivity(),"No internet connection.",Toast.LENGTH_LONG).show()
        }
    }

    private fun getBanner(
        token: String,
    ){
        binding.imageSlider.gone()
        binding.bannerShimmerView.visible()
        binding.bannerShimmerView.startShimmer()
        mGetBannerViewModel.getBanner(token).observe(viewLifecycleOwner) { outcome ->
            when (outcome) {
                is Outcome.Success -> {
                    if (outcome.data.status == 1) {
                        binding.swipeRefreshLayout.setRefreshing(false)

                        binding.imageSlider.visible()
                        binding.bannerShimmerView.gone()
                        binding.bannerShimmerView.stopShimmer()
                        //Toast.makeText(requireActivity(), outcome.data.result.message, Toast.LENGTH_SHORT).show()

                        if(outcome.data.result.result.banner != null && outcome.data.result.result.banner.size>0){
                            val imageList = ArrayList<SlideModel>() // Create image list
                            for (i in outcome.data.result.result.banner){
                                imageList.add(SlideModel(ApiConstants.PUBLIC_URL+i.banner_image))
                                binding.imageSlider.setImageList(imageList)
                            }
                            binding.bannerLay.visible()
                        }else{
                            binding.bannerLay.gone()
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

    private fun getUpcommingCourses(
        token: String,
    ){
        binding.upcomingCourseRecycler.gone()
        binding.upcomingShimmerView.visible()
        binding.upcomingShimmerView.startShimmer()
        mUpcommingCoursesViewModel.getUpcommingCourses(token).observe(viewLifecycleOwner) { outcome ->
            when (outcome) {
                is Outcome.Success -> {
                    if (outcome.data.status == 1) {
                        binding.swipeRefreshLayout.setRefreshing(false)

                        binding.upcomingShimmerView.stopShimmer()
                        binding.upcomingShimmerView.gone()
                        binding.upcomingCourseRecycler.visible()

                        if(outcome.data.result.result.size>0 && outcome.data.result.result != null){
                            binding.upcomingHtv.visible()
                            binding.upcomingHtv2.visible()
                            fillUpcomingRecyclerView(outcome.data.result.result)
                        }else{
                            binding.upcomingHtv.gone()
                            binding.upcomingHtv2.gone()
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

    private fun getSuggestionCourses(
        token: String,
    ){
        binding.suggestionCourseRecycler.gone()
        binding.suggestionShimmerView.visible()
        binding.suggestionShimmerView.startShimmer()
        mGetSuggestionCoursesViewModel.getSuggestionCourses(token).observe(viewLifecycleOwner) { outcome ->
            when (outcome) {
                is Outcome.Success -> {
                    if (outcome.data.status == 1) {
                        binding.swipeRefreshLayout.setRefreshing(false)

                        binding.suggestionShimmerView.stopShimmer()
                        binding.suggestionShimmerView.gone()
                        binding.suggestionCourseRecycler.visible()
                        if(outcome.data.result.result.size>0 && outcome.data.result.result != null){
                            binding.suggestionHtv.visible()
                            binding.suggestionHtv2.visible()
                            binding.suggestionViewAllBtn.visible()
                            fillSuggestionRecyclerView(outcome.data.result.result)
                        }else{
                            binding.suggestionHtv.gone()
                            binding.suggestionHtv2.gone()
                            binding.suggestionViewAllBtn.gone()
                        }
                    } else {
                        Toast.makeText(requireActivity(), outcome.data.result.message, Toast.LENGTH_SHORT).show()
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

    override fun onClick(view: View, id: Int) {
        val intent = Intent(requireActivity(), CourseDetailsActivity::class.java)
        intent.putExtra("id",id)
        intent.putExtra("show_enroll",true)
        startActivity(intent)
    }
}