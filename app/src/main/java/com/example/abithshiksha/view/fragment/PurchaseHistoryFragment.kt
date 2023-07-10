package com.example.abithshiksha.view.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.abithshiksha.R
import com.example.abithshiksha.databinding.FragmentPurchaseHistoryBinding
import com.example.abithshiksha.helper.PrefManager
import com.example.abithshiksha.helper.click_listener.TopicClickListener
import com.example.abithshiksha.model.network.ApiConstants
import com.example.abithshiksha.model.pojo.TestModel
import com.example.abithshiksha.model.pojo.puchase_history.ResultX
import com.example.abithshiksha.model.repo.Outcome
import com.example.abithshiksha.view.adapter.PurchaseHistoryListAdapter
import com.example.abithshiksha.view_model.GetPurchaseHistoryViewModel
import com.example.abithshiksha.view_model.GetPurchaseReceiptViewModel
import com.user.caregiver.gone
import com.user.caregiver.isConnectedToInternet
import com.user.caregiver.loadingDialog
import com.user.caregiver.visible
import org.koin.androidx.viewmodel.ext.android.viewModel

class PurchaseHistoryFragment : Fragment(), TopicClickListener {
    private var _binding: FragmentPurchaseHistoryBinding? = null
    private val binding get() = _binding!!

    private val mGetPurchaseHistoryViewModel: GetPurchaseHistoryViewModel by viewModel()
    private val mGetPurchaseReceiptViewModel: GetPurchaseReceiptViewModel by viewModel()

    private lateinit var accessToken: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPurchaseHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //get token
        accessToken = "Bearer "+PrefManager.getKeyAuthToken()

        binding.emptyListLot.gone()
        binding.noDataHtv.gone()

    }

    override fun onResume() {
        binding.purchaseRecycler.gone()
        binding.purchaseShimmerView.startShimmer()
        binding.emptyListLot.gone()
        binding.noDataHtv.gone()
        if(requireActivity().isConnectedToInternet()){
            getPurchaseHistory(accessToken)
        }else{
            Toast.makeText(requireActivity(),"No internet connection.", Toast.LENGTH_SHORT).show()
        }
        super.onResume()
    }

    private fun fillRecycler(list: List<ResultX>) {
        val gridLayoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        binding.purchaseRecycler.apply {
            layoutManager = gridLayoutManager
            adapter = PurchaseHistoryListAdapter(list,requireActivity(), this@PurchaseHistoryFragment)
        }
    }

    private fun getPurchaseHistory(
        token: String
    ) {
        mGetPurchaseHistoryViewModel.getPurchaseHistory(token)
            .observe(requireActivity()) { outcome ->
                when (outcome) {
                    is Outcome.Success -> {
                        if (outcome.data.status == 1) {
                            binding.purchaseShimmerView.stopShimmer()
                            binding.purchaseShimmerView.gone()

                            if(outcome.data.result.result.isNotEmpty() && outcome.data.result.result != null){
                                binding.purchaseRecycler.visible()
                                binding.emptyListLot.gone()
                                binding.noDataHtv.gone()
                                fillRecycler(outcome.data.result.result)
                            }else{
                                binding.purchaseRecycler.gone()
                                binding.emptyListLot.visible()
                                binding.noDataHtv.visible()
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

    private fun getReceipt(
        token: String,
        order_id: Int
    ) {
        val loader = requireActivity().loadingDialog(false,R.raw.loader_bar)
        loader.show()
        mGetPurchaseReceiptViewModel.getReceipt(token, order_id)
            .observe(requireActivity()) { outcome ->
                loader.dismiss()
                when (outcome) {
                    is Outcome.Success -> {
                        if (outcome.data.status == 1) {
                            Toast.makeText(
                                requireActivity(),
                                outcome.data.result.message,
                                Toast.LENGTH_SHORT
                            ).show()
                            downloadInvoice(outcome.data.result.invoice_url)
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

    private fun downloadInvoice(url: String){
        val url = ApiConstants.PUBLIC_URL+url
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
    }

    override fun onTopicClick(view: View, id: Int) {
        getReceipt(accessToken, id)
    }
}