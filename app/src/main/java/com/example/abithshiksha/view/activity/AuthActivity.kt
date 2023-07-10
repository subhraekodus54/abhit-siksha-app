package com.example.abithshiksha.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.abithshiksha.R
import com.example.abithshiksha.databinding.ActivityAuthBinding
import com.example.abithshiksha.databinding.ActivitySplashBinding
import com.user.caregiver.lightStatusBar

class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lightStatusBar(this,R.color.white)

    }
}