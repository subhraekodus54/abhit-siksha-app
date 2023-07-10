package com.example.abithshiksha.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.abithshiksha.MainActivity
import com.example.abithshiksha.R
import com.example.abithshiksha.databinding.ActivityNoInternetBinding
import com.example.abithshiksha.databinding.ActivitySplashBinding
import com.example.abithshiksha.helper.PrefManager
import com.thekhaeng.pushdownanim.PushDownAnim
import com.user.caregiver.isConnectedToInternet
import com.user.caregiver.lightStatusBar

class NoInternetActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNoInternetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoInternetBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lightStatusBar(this,R.color.white)

        PushDownAnim.setPushDownAnimTo(binding.retryBtn).setOnClickListener {
            checkInternet()
        }
    }


    private fun checkInternet(){
        if(isConnectedToInternet()){
            if(PrefManager.getLogInStatus() == true){
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                val intent = Intent(this, AuthActivity::class.java)
                startActivity(intent)
                finish()
            }
        }else{
            Toast.makeText(this,"No internet connection, please retry.", Toast.LENGTH_SHORT).show()
        }
    }
}