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
import com.example.abithshiksha.databinding.FragmentAddOnsBinding
import com.example.abithshiksha.helper.PrefManager
import com.example.abithshiksha.model.pojo.get_addons.SelectedAddon
import com.example.abithshiksha.model.repo.Outcome
import com.example.abithshiksha.view.adapter.ShowAddonsAdapter
import com.example.abithshiksha.view_model.GetSelectedAddonViewModel
import com.user.caregiver.gone
import com.user.caregiver.visible
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddOnsFragment(private val topic_id: Int) : Fragment() {
    private var _binding: FragmentAddOnsBinding? = null
    private val binding get() = _binding!!
    private val mGetSelectedAddonViewModel: GetSelectedAddonViewModel by viewModel()
    private lateinit var accessToken: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddOnsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //get token
        accessToken = "Bearer "+ PrefManager.getKeyAuthToken()

        getAddons(accessToken)
    }

    private fun fillRecycler(list: List<SelectedAddon>) {
        val linearLayoutManager = LinearLayoutManager(requireActivity())
        binding.addOnsRecycler.apply {
            layoutManager = linearLayoutManager
            setHasFixedSize(true)
            isFocusable = false
            adapter = ShowAddonsAdapter(list.toMutableList(),requireActivity())
        }
    }

    private fun getAddons(
        token: String,
    ){
        mGetSelectedAddonViewModel.getSelectedAddons(token).observe(viewLifecycleOwner) { outcome ->
            when (outcome) {
                is Outcome.Success -> {
                    if (outcome.data.status == 1) {
                        binding.articleShimmerView.stopShimmer()
                        binding.articleShimmerView.gone()
                        if(outcome.data.result.selected_addons.isNotEmpty()){
                            binding.addOnsRecycler.visible()
                            binding.emptyListLot.gone()
                            binding.noDataHtv.gone()
                            outcome.data.result.selected_addons?.let {
                                fillRecycler(it)
                            }
                        }else{
                            binding.addOnsRecycler.gone()
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