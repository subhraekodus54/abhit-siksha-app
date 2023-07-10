package com.example.abithshiksha.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import com.example.abithshiksha.R
import com.example.abithshiksha.databinding.ActivityEditProfleBinding
import com.example.abithshiksha.databinding.ActivityProfileBinding
import com.example.abithshiksha.helper.PrefManager
import com.example.abithshiksha.model.repo.Outcome
import com.example.abithshiksha.view_model.AddToCartViewModel
import com.example.abithshiksha.view_model.EditProfileViewModel
import com.thekhaeng.pushdownanim.PushDownAnim
import com.user.caregiver.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfleBinding
    private val mEditProfileViewModel: EditProfileViewModel by viewModel()
    private lateinit var accessToken: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lightStatusBar(this,R.color.white)
        binding.progressBar.gone()
        //get token
        accessToken = "Bearer "+ PrefManager.getKeyAuthToken()

        val extras = intent.extras
        if (extras != null) {
            val name: String? = intent?.getStringExtra("name")
            val address: String? = intent?.getStringExtra("address")
            val academic: String? = intent?.getStringExtra("academic")
            name?.let {
                binding.nameTxt.text = Editable.Factory.getInstance().newEditable(it)
            }
            address?.let {
                binding.addressTxt.text = Editable.Factory.getInstance().newEditable(it)
            }
            academic?.let {
                binding.academicTxt.text = Editable.Factory.getInstance().newEditable(it)
            }
        }

        //validation
        nameFocusListener()

        binding.backArrow.setOnClickListener {
            finish()
        }

        PushDownAnim.setPushDownAnimTo(binding.updateBtn).setOnClickListener {
            val name: String? = binding.nameTxt.text.toString()
            val address: String? = binding.addressTxt.text.toString()
            val academic: String? = binding.academicTxt.text.toString()

            val validName = binding.nameTxtLay.helperText == null

            if(name != null && name != "" && validName){
                if(isConnectedToInternet()){
                    hideSoftKeyboard()
                    editProfile(academic,name,address,accessToken)
                }else{
                    Toast.makeText(this,"No internet connection.",Toast.LENGTH_SHORT).show()
                }
            }else{
                //Toast.makeText(this,"Please provide your name.",Toast.LENGTH_SHORT).show()
                Toast.makeText(this,binding.nameTxtLay.helperText.toString(),Toast.LENGTH_SHORT).show()
                binding.nameTxt.showKeyboard()
            }
        }
    }

    private fun nameFocusListener(){
        binding.nameTxt.doOnTextChanged { text, start, before, count ->
            binding.nameTxtLay.helperText = validName()
        }
    }

    private fun validName(): String? {
        val nameText = binding.nameTxt.text.toString()

        if(nameText.isEmpty()){
            return "Provide your name."
        }
        return null
    }

    private fun editProfile(
        education: String?,
        gender: String?,
        address: String?,
        token: String
    ){
        binding.progressBar.visible()
        mEditProfileViewModel.editProfile(education, gender, address, token).observe(this) { outcome ->
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