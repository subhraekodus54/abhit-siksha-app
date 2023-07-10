package com.example.abithshiksha.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.abithshiksha.R
import com.example.abithshiksha.databinding.ActivitySubTopicBinding
import com.example.abithshiksha.databinding.ActivityWebViewBinding
import com.thekhaeng.pushdownanim.PushDownAnim
import com.user.caregiver.gone
import com.user.caregiver.lightStatusBar
import com.user.caregiver.visible

class WebViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWebViewBinding
    private lateinit var url:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lightStatusBar(this,R.color.white)

        val extras = intent.extras
        if (extras != null) {
            url = intent?.getStringExtra("url")!!
            binding.webView.loadUrl(url)
        }

        PushDownAnim.setPushDownAnimTo(binding.backArrow).setOnClickListener {
            finish()
        }

        //val url = "https://learn.microsoft.com/en-us/answers/questions/874469/open-external-browser-on-tapping-on-link-with-targ.html"

        binding.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)

                binding.progressBar.visible()
                return true
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
            }
            override fun onPageCommitVisible(view: WebView?, url: String?) {
                super.onPageCommitVisible(view, url)
                binding.progressBar.gone()
            }
        }
    }
}