package com.example.abithshiksha.view.activity

import android.content.Intent
import android.os.Bundle
import android.util.Base64
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.abithshiksha.R
import com.example.abithshiksha.databinding.ActivityViewArticleBinding
import com.thekhaeng.pushdownanim.PushDownAnim
import com.user.caregiver.gone
import com.user.caregiver.lightStatusBar
import com.user.caregiver.visible
import org.sufficientlysecure.htmltextview.HtmlResImageGetter
import java.util.Base64.getEncoder


class ViewArticleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityViewArticleBinding
    private lateinit var content: String
    private lateinit var title: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lightStatusBar(this,R.color.white)

        val extras = intent.extras
        if (extras != null) {
            content = intent?.getStringExtra("content")!!
            title = intent?.getStringExtra("title")!!
            binding.titleTv.text = title
        }

        PushDownAnim.setPushDownAnimTo(binding.backArrow).setOnClickListener {
            finish()
        }

        content?.let { content ->

            val encodedString: String = Base64.encodeToString(content.toByteArray(), Base64.NO_PADDING)

            binding.webView.requestFocus();
            binding.webView.settings.lightTouchEnabled = true
            binding.webView.settings.setGeolocationEnabled(true)
            binding.webView.isSoundEffectsEnabled = true
            binding.webView.settings.javaScriptEnabled = true
            binding.webView.settings.javaScriptCanOpenWindowsAutomatically = true
            //binding.webView.loadData(content, "text/html; charset=utf-8", "UTF-8")
            binding.webView.loadData(encodedString, "text/html", "base64")

            binding.webView.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                    /*//view.loadData(content, "text/html; charset=utf-8", "UTF-8")
                    view.loadData(encodedString, "text/html", "base64")
                    binding.progressBar.visible()*/

                    val intent = Intent(this@ViewArticleActivity, WebViewActivity::class.java)
                    intent.putExtra("url", url)
                    startActivity(intent)

                    //view.loadUrl(url)

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
}