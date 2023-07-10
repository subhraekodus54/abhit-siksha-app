package com.example.abithshiksha.view.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.example.abithshiksha.R
import com.example.abithshiksha.databinding.FragmentForgotPassEmailNoBinding
import com.example.abithshiksha.databinding.FragmentForgotPassOtpBinding
import com.example.abithshiksha.model.repo.Outcome
import com.example.abithshiksha.view_model.SignUpViewModel
import com.example.abithshiksha.view_model.VerifyFpOtpViewModel
import com.user.caregiver.gone
import com.user.caregiver.hideSoftKeyboard
import com.user.caregiver.loadingDialog
import com.user.caregiver.visible
import org.koin.androidx.viewmodel.ext.android.viewModel

class ForgotPassOtpFragment : Fragment() {
    private var _binding: FragmentForgotPassOtpBinding? = null
    private val binding get() = _binding!!
    private val mVerifyFpOtpViewModel: VerifyFpOtpViewModel by viewModel()
    private var user_id: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            user_id = it.getString("user_id").toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentForgotPassOtpBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.progressBar.gone()


        binding.edTxt1.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if(binding.edTxt1.text.length == 1){
                    binding.edTxt2.requestFocus()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        binding.edTxt2.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if(binding.edTxt2.text.length == 1){
                    binding.edTxt3.requestFocus()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        binding.edTxt3.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if(binding.edTxt3.text.length == 1){
                    binding.edTxt4.requestFocus()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        binding.edTxt4.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if(binding.edTxt4.text.length == 1){
                    binding.edTxt5.requestFocus()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        binding.edTxt5.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if(binding.edTxt5.text.length == 1){
                    binding.edTxt6.requestFocus()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })


        binding.backArrow.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.sendOtpBtn.setOnClickListener {
            val otp1 = binding.edTxt1.text.toString()
            val otp2 = binding.edTxt2.text.toString()
            val otp3 = binding.edTxt3.text.toString()
            val otp4 = binding.edTxt4.text.toString()
            val otp5 = binding.edTxt5.text.toString()
            val otp6 = binding.edTxt6.text.toString()
            val otp = binding.edTxt1.text.toString()+binding.edTxt2.text.toString()+binding.edTxt3.text.toString()+
                    binding.edTxt4.text.toString()+binding.edTxt5.text.toString()+binding.edTxt6.text.toString()
            if (!user_id.isEmpty()){
                if(!otp1.isEmpty() && !otp2.isEmpty() && !otp3.isEmpty() && !otp4.isEmpty()
                    && !otp5.isEmpty() && !otp6.isEmpty()){
                    requireActivity().hideSoftKeyboard()
                    verifyOtp(user_id,otp)
                }else{
                    Toast.makeText(requireActivity(),"Please provide the OTP.", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(requireActivity(),"There is a technical glitch, please try again.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun verifyOtp(
        user_id: String,
        otp: String,
    ){
        val loader = requireActivity().loadingDialog(false,R.raw.loader_bar)
        loader.show()
        mVerifyFpOtpViewModel.verifyOtp(user_id, otp).observe(viewLifecycleOwner) { outcome ->
            loader.dismiss()
            when (outcome) {
                is Outcome.Success -> {
                    if (outcome.data.status == 1) {
                        Toast.makeText(requireActivity(), outcome.data.result.message, Toast.LENGTH_SHORT).show()
                        val bundle = Bundle()
                        bundle.putString("user_id", user_id)
                        findNavController().navigate(R.id.action_forgotPassOtpFragment_to_forgotPassNewPassFragment,bundle)
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
}