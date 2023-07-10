package com.example.abithshiksha.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.abithshiksha.R
import com.example.abithshiksha.databinding.ActivityEnrollNowBinding
import com.example.abithshiksha.databinding.ActivityJobsBinding

class JobsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityJobsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJobsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}