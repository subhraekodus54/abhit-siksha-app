package com.example.abithshiksha.view.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import com.example.abithshiksha.MainActivity
import com.example.abithshiksha.R
import com.example.abithshiksha.databinding.FragmentHomeBinding
import com.example.abithshiksha.databinding.FragmentLoginBinding
import com.example.abithshiksha.helper.PrefManager
import com.example.abithshiksha.model.repo.Outcome
import com.example.abithshiksha.view.activity.AuthActivity
import com.example.abithshiksha.view_model.LoginViewModel
import com.google.android.material.snackbar.Snackbar
import com.thekhaeng.pushdownanim.PushDownAnim
import com.user.caregiver.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val mLoginViewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val root: View = binding.root
        //checkInternetConnection()
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.progressBar.gone()
        emailFocusListener()
        passwordFocusListener()

        PushDownAnim.setPushDownAnimTo(binding.registerNowHtv).setOnClickListener {
            findNavController().navigate(R.id.registrationFragment)
        }
        PushDownAnim.setPushDownAnimTo(binding.loginForgotPassBtn).setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_forgotPassEmailNoFragment)
        }
        binding.loginBtn.setOnClickListener {
            val email_valid = validEmail() == null
            val pass_valid = validPassword() == null

            if(binding.emailTxt.text.toString().isNotEmpty()){
                if(email_valid){
                    if(pass_valid){
                        if(binding.passwordTxt.text.toString().isNotEmpty()){
                            requireActivity().hideSoftKeyboard()
                            if(requireActivity().isConnectedToInternet()){
                                login(binding.emailTxt.text.toString(), binding.passwordTxt.text.toString())
                            }else{
                                Toast.makeText(requireActivity(),"No Internet Connection",Toast.LENGTH_SHORT).show()
                            }
                        }else{
                            binding.passwordTxt.showKeyboard()
                            Toast.makeText(requireActivity(),"Please provide the password",Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        Toast.makeText(requireActivity(),binding.passwordTxtLay.helperText.toString(),Toast.LENGTH_SHORT).show()
                        binding.passwordTxt.showKeyboard()
                    }
                }else{
                    Toast.makeText(requireActivity(),binding.emailTxtLay.helperText.toString(),Toast.LENGTH_SHORT).show()
                    binding.emailTxt.showKeyboard()
                }
            }else{
                binding.emailTxt.showKeyboard()
                Toast.makeText(requireActivity(),"Please provide the email address",Toast.LENGTH_SHORT).show()
            }
        }
    }

/*
    private fun checkInternetConnection(){
        if(requireActivity().isConnectedToInternet()){
            binding.mainView.visible()
            binding.noInternetView.gone()
        }else{
            binding.mainView.gone()
            binding.noInternetView.visible()
            Toast.makeText(requireActivity(),"Oops!! No internet connection",Toast.LENGTH_SHORT).show()

            */
/*PushDownAnim.setPushDownAnimTo(binding.retryBtn).setOnClickListener {
                *//*
*/
/*Snackbar.make(it, "Oops! No internet connection", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()*//*
*/
/*
                //checkInternetConnection()
            }*//*

        }
    }
*/


    private fun emailFocusListener(){
        binding.emailTxt.doOnTextChanged { text, start, before, count ->
            binding.emailTxtLay.helperText = validEmail()
        }
    }

    private fun validEmail(): String? {
        val emailText = binding.emailTxt.text.toString()

        if(!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()){
            return "Invalid Email Address"
        }

        if(emailText.isEmpty()){
            return "Provide your registered email address"
        }
        return null
    }

    private fun passwordFocusListener(){
        binding.passwordTxt.doOnTextChanged { text, start, before, count ->
            binding.passwordTxtLay.helperText = validPassword()
        }
    }

    private fun validPassword(): String? {
        val passwordText = binding.passwordTxt.text.toString()

        if(passwordText.isEmpty()){
            return "Provide your password"
        }
        return null
    }

    private fun login(
        email: String,
        password: String,
    ){
        val loader = requireActivity().loadingDialog(false,R.raw.loader_bar)
        loader.show()
        mLoginViewModel.login(email, password).observe(viewLifecycleOwner) { outcome ->
            loader.dismiss()
            when (outcome) {
                is Outcome.Success -> {
                    if (outcome.data.status == 1) {
                        if (outcome.data.access_token != null) {
                            PrefManager.setKeyAuthToken(outcome.data.access_token)
                        }
                        PrefManager.setLogInStatus(true)

                        startActivity(Intent(requireActivity(), MainActivity::class.java))
                        requireActivity().finish()
                        Toast.makeText(requireActivity(), outcome.data.message, Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(requireActivity(), outcome.data.message, Toast.LENGTH_SHORT)
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