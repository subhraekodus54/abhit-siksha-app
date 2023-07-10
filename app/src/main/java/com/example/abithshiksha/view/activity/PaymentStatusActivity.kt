package com.example.abithshiksha.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.abithshiksha.MainActivity
import com.example.abithshiksha.R
import com.example.abithshiksha.databinding.ActivityCartBinding
import com.example.abithshiksha.databinding.ActivityPaymentStatusBinding
import com.thekhaeng.pushdownanim.PushDownAnim
import com.user.caregiver.lightStatusBar

class PaymentStatusActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPaymentStatusBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentStatusBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lightStatusBar(this,R.color.white)

        PushDownAnim.setPushDownAnimTo(binding.okayBtn).setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}