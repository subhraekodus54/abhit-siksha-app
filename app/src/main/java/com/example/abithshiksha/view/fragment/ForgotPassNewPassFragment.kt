package com.example.abithshiksha.view.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import com.example.abithshiksha.R
import com.example.abithshiksha.databinding.FragmentForgotPassNewPassBinding
import com.example.abithshiksha.databinding.FragmentForgotPassOtpBinding
import com.example.abithshiksha.model.repo.Outcome
import com.example.abithshiksha.view_model.FpChangePassViewModel
import com.example.abithshiksha.view_model.VerifyFpOtpViewModel
import com.thekhaeng.pushdownanim.PushDownAnim
import com.user.caregiver.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ForgotPassNewPassFragment : Fragment() {
    private var _binding: FragmentForgotPassNewPassBinding? = null
    private val binding get() = _binding!!

    private val mFpChangePassViewModel: FpChangePassViewModel by viewModel()
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
        _binding = FragmentForgotPassNewPassBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        newPasswordFocusListener()
        conPasswordFocusListener()

        PushDownAnim.setPushDownAnimTo(binding.backArrow).setOnClickListener {
            findNavController().navigate(R.id.action_forgotPassNewPassFragment_to_loginFragment)
        }

        PushDownAnim.setPushDownAnimTo(binding.updatePassBtn).setOnClickListener {
            if(!user_id.isEmpty()){
                val validNewPassword = binding.newPassTxtLay.helperText == null
                val validConPassword = binding.conPassTxtLay.helperText == null

                if(validNewPassword){
                    if(validConPassword){
                        if(binding.newPasswordTxt.text.toString() == binding.conPasswordTxt.text.toString()){
                            if(requireActivity().isConnectedToInternet()){
                                requireActivity().hideSoftKeyboard()
                                changePass(user_id,binding.newPasswordTxt.text.toString())
                            }else{
                                Toast.makeText(requireActivity(),"No internet connection.", Toast.LENGTH_SHORT).show()
                            }
                        }else{
                            Toast.makeText(requireActivity(),"Confirm password mismatch with the new password.", Toast.LENGTH_SHORT).show()
                            binding.conPassTxtLay.showKeyboard()
                        }
                    }else{
                        Toast.makeText(requireActivity(),binding.conPassTxtLay.helperText.toString(), Toast.LENGTH_SHORT).show()
                        binding.conPassTxtLay.showKeyboard()
                    }
                }else{
                    Toast.makeText(requireActivity(),binding.newPassTxtLay.helperText.toString(), Toast.LENGTH_SHORT).show()
                    binding.newPasswordTxt.showKeyboard()
                }
            }else{
                Toast.makeText(requireActivity(),"There is a technical glitch, please try again.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun newPasswordFocusListener() {
        binding.newPasswordTxt.doOnTextChanged { text, start, before, count ->
            binding.newPassTxtLay.helperText = validNewPassword()
        }
    }
    private fun validNewPassword(): String? {
        val passwordText = binding.newPasswordTxt.text.toString()
        if(passwordText.length < 6){
            return "Minimum 6 characters required."
        }
        if(!passwordText.matches(".*[A-Z].*".toRegex())){
            return "Must contain 1 upper case character."
        }
        if(!passwordText.matches(".*[a-z].*".toRegex())){
            return "Must contain 1 lower case character."
        }
        if(!passwordText.matches(".*[0-9].*".toRegex())){
            return "Must contain at least 1 number."
        }
        if(!passwordText.matches(".*[!@$#%&*_-].*".toRegex())){
            return "Must contain 1 special character (!@$#%&*_-)."
        }
        return  null
    }

    private fun conPasswordFocusListener() {
        binding.conPasswordTxt.doOnTextChanged { text, start, before, count ->
            binding.conPassTxtLay.helperText = validConPassword()
        }
    }
    private fun validConPassword(): String? {
        val new_passwordText = binding.newPasswordTxt.text.toString()
        val con_passwordText = binding.conPasswordTxt.text.toString()

        if(con_passwordText != new_passwordText){
            return "Password mismatch with the new password."
        }
        return  null
    }


    private fun changePass(
        user_id: String,
        password: String,
    ){
        val loader = requireActivity().loadingDialog(false,R.raw.loader_bar)
        loader.show()
        mFpChangePassViewModel.changePass(user_id, password).observe(viewLifecycleOwner) { outcome ->
            loader.dismiss()
            when (outcome) {
                is Outcome.Success -> {
                    if (outcome.data.status == 1) {
                        Toast.makeText(requireActivity(), outcome.data.result.message, Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_forgotPassNewPassFragment_to_loginFragment)
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