package com.example.abithshiksha.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.abithshiksha.MainActivity
import com.example.abithshiksha.R
import com.example.abithshiksha.databinding.ActivityMainBinding
import com.example.abithshiksha.databinding.ActivitySplashBinding
import com.example.abithshiksha.helper.PrefManager
import com.user.caregiver.isConnectedToInternet
import com.user.caregiver.lightStatusBar

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lightStatusBar(this,R.color.theme_blue)

        checkInternet()
    }

    private fun checkInternet(){

        Handler(Looper.getMainLooper()).postDelayed({

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
                val intent = Intent(this, NoInternetActivity::class.java)
                startActivity(intent)
                finish()
            } }, 3000)

    }
}