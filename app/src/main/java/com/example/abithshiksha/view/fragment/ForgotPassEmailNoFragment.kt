package com.example.abithshiksha.view.fragment

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.abithshiksha.R
import com.example.abithshiksha.databinding.FragmentForgotPassEmailNoBinding
import com.example.abithshiksha.helper.PrefManager
import com.example.abithshiksha.model.repo.Outcome
import com.example.abithshiksha.view_model.SendFpOtpViewModel
import com.user.caregiver.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ForgotPassEmailNoFragment : Fragment() {
    private var _binding: FragmentForgotPassEmailNoBinding? = null
    private val binding get() = _binding!!
    val typeList: Array<String> =  arrayOf("Select mobile or email","Mobile number","Email address")
    var type:String = ""
    var global_type: String = ""
    var user_id: String = ""

    private val mSendFpOtpViewModel: SendFpOtpViewModel by viewModel()
    private lateinit var accessToken: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentForgotPassEmailNoBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.progressBar.gone()

        //get token
        accessToken = "Bearer " + PrefManager.getKeyAuthToken()

        mobileFocusListener()
        emailFocusListener()

        binding.backArrow.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.sendOtpBtn.setOnClickListener {
            val phone = binding.mobileTxt.text.toString()
            val email = binding.emailTxt.text.toString()
            if(!global_type.isEmpty()){
                if(global_type == "1"){
                    val validNumber = binding.mobileTxtLay.helperText == null

                    if(validNumber){
                        requireActivity().hideSoftKeyboard()
                        user_id = phone
                        sendOtp(global_type,phone,null,accessToken)
                    }else{
                        Toast.makeText(requireActivity(),binding.mobileTxtLay.helperText.toString(),Toast.LENGTH_SHORT).show()
                        binding.mobileTxt.showKeyboard()
                    }
                }else if(global_type == "2"){

                    val validEmail = binding.emailLay.helperText == null
                    if(validEmail){
                        requireActivity().hideSoftKeyboard()
                        user_id = email
                        sendOtp(global_type,null,email,accessToken)
                    }else{
                        Toast.makeText(requireActivity(),binding.emailLay.helperText.toString(),Toast.LENGTH_SHORT).show()
                        binding.emailTxt.showKeyboard()
                    }
                }
            }else{
                Toast.makeText(requireActivity(),"Please select a type",Toast.LENGTH_SHORT).show()
            }
        }

        //state spinner
        val arrayAdapter = ArrayAdapter(requireActivity(),android.R.layout.simple_spinner_dropdown_item,typeList)
        binding.typeSpinner.adapter = arrayAdapter
        binding.typeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener,
            AdapterView.OnItemClickListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                type = typeList[p2]
                if(type == "Mobile number"){
                    binding.emailLay.gone()
                    binding.mobileTxtLay.visible()
                    binding.sendOtpBtn.visible()
                    global_type = "1"
                }else if(type == "Email address"){
                    binding.emailLay.visible()
                    binding.mobileTxtLay.gone()
                    binding.sendOtpBtn.visible()
                    global_type = "2"
                }else{
                    binding.emailLay.gone()
                    binding.mobileTxtLay.gone()
                    binding.sendOtpBtn.gone()
                    global_type = ""
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                TODO("Not yet implemented")
            }

        }
    }

    private fun mobileFocusListener(){
        binding.mobileTxt.doOnTextChanged { text, start, before, count ->
            binding.mobileTxtLay.helperText = validMobileNumber()
        }
    }

    private fun validMobileNumber(): String? {
        val mobileText = binding.mobileTxt.text.toString()

        if(mobileText.length != 10){
            return "10 digit number required."
        }
        if(binding.mobileTxt.text.toString().toDouble() == 0.00){
            return "Please provide a valid phone number."
        }
        return  null
    }

    private fun emailFocusListener(){
        binding.emailTxt.doOnTextChanged { text, start, before, count ->
            binding.emailLay.helperText = validEmail()
        }
    }

    private fun validEmail(): String? {
        val emailText = binding.emailTxt.text.toString()
        if(!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()){
            binding.emailTxt.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
            return "Invalid Email Address"
        }
        return null
    }

    private fun sendOtp(
        type: String,
        phone: String?,
        email: String?,
        token: String
    ) {
        val loader = requireActivity().loadingDialog(false,R.raw.loader_bar)
        loader.show()
        mSendFpOtpViewModel.sendOtp(type, phone, email, token)
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

                            val bundle = Bundle()
                            bundle.putString("user_id", user_id)
                            findNavController().navigate(R.id.action_forgotPassEmailNoFragment_to_forgotPassOtpFragment,bundle)

                        } else {
                            Toast.makeText(
                                requireActivity(),
                                outcome.data.result.message,
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
}