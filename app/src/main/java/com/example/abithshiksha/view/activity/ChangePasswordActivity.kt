package com.example.abithshiksha.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import com.example.abithshiksha.R
import com.example.abithshiksha.databinding.ActivityCartBinding
import com.example.abithshiksha.databinding.ActivityChangePasswordBinding
import com.example.abithshiksha.helper.PrefManager
import com.example.abithshiksha.model.repo.Outcome
import com.example.abithshiksha.view_model.ChangePasswordViewModel
import com.example.abithshiksha.view_model.GetCartDetailsViewModel
import com.thekhaeng.pushdownanim.PushDownAnim
import com.user.caregiver.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChangePasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChangePasswordBinding
    private lateinit var accessToken: String
    private val mChangePasswordViewModel: ChangePasswordViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lightStatusBar(this,R.color.white)
        binding.progressBar.gone()

        //get token
        accessToken = "Bearer "+ PrefManager.getKeyAuthToken()

        //validation
        newPasswordFocusListener()
        oldPasswordFocusListener()
        conPasswordFocusListener()

        binding.backArrow.setOnClickListener {
            finish()
        }

        PushDownAnim.setPushDownAnimTo(binding.updatePassBtn).setOnClickListener {
            updatePasswordClick()
        }
    }

    private fun oldPasswordFocusListener() {
        binding.oldPasswordTxt.doOnTextChanged { text, start, before, count ->
            binding.oldPassTxtLay.helperText = validOldPassword()
        }
    }
    private fun validOldPassword(): String? {
        val passwordText = binding.oldPasswordTxt.text.toString()
        if(passwordText.length == 0){
            return "Required."
        }
        return  null
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

    private fun updatePasswordClick(){
        val validOldPassword = binding.oldPassTxtLay.helperText == null
        val validNewPassword = binding.newPassTxtLay.helperText == null
        val validConPassword = binding.conPassTxtLay.helperText == null
        if(validOldPassword){
            if(validNewPassword){
                if(validConPassword){
                    if(binding.newPasswordTxt.text.toString() == binding.conPasswordTxt.text.toString()){
                        if(isConnectedToInternet()){
                            changePassword(binding.oldPasswordTxt.text.toString(),binding.newPasswordTxt.text.toString(),accessToken)
                        }else{
                            Toast.makeText(this,"No internet connection.", Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        Toast.makeText(this,"Confirm password mismatch with the new password.", Toast.LENGTH_SHORT).show()
                        binding.conPassTxtLay.showKeyboard()
                    }
                }else{
                    Toast.makeText(this,binding.conPassTxtLay.helperText.toString(), Toast.LENGTH_SHORT).show()
                    binding.conPassTxtLay.showKeyboard()
                }
            }else{
                Toast.makeText(this,binding.newPassTxtLay.helperText.toString(), Toast.LENGTH_SHORT).show()
                binding.newPasswordTxt.showKeyboard()
            }
        }else{
            Toast.makeText(this,binding.oldPassTxtLay.helperText.toString(), Toast.LENGTH_SHORT).show()
            binding.oldPasswordTxt.showKeyboard()
        }
    }

    private fun changePassword(
        old_pass: String,
        new_pass: String,
        token: String
    ){
        binding.progressBar.visible()
        mChangePasswordViewModel.changePassword(old_pass, new_pass, token).observe(this) { outcome ->
            binding.progressBar.gone()
            when (outcome) {
                is Outcome.Success -> {
                    if (outcome.data.status == 1) {
                        Toast.makeText(this, outcome.data.result.message, Toast.LENGTH_SHORT)
                            .show()
                        finish()
                    } else {
                        Toast.makeText(this, outcome.data.result.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                is Outcome.Failure<*> -> {
                    Toast.makeText(this, R.string.network_issue, Toast.LENGTH_SHORT).show()

                    Log.i("statusMsg", outcome.e.message.toString())

                    outcome.e.printStackTrace()
                    Log.i("status", outcome.e.cause.toString())
                }
            }
        }
    }
}