package com.example.abithshiksha.view.activity

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.viewModels
import androidx.core.net.toFile
import com.example.abithshiksha.R
import com.example.abithshiksha.databinding.ActivityPdfViewerBinding
import com.example.abithshiksha.databinding.ActivityViewArticleBinding
import com.example.abithshiksha.view_model.PdfViewModel
import com.example.abithshiksha.view_model.PdfViewModelFactory
import com.pdfview.PDFView
import com.user.caregiver.gone
import com.user.caregiver.lightStatusBar
import com.user.caregiver.loadingDialog
import com.user.caregiver.visible

class PdfViewerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPdfViewerBinding
    private lateinit var url:String
    private lateinit var name: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPdfViewerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lightStatusBar(this,R.color.white)

        val extras = intent.extras
        if (extras != null) {
            url = intent?.getStringExtra("url")!!
            name = intent?.getStringExtra("name")!!

            val pdfViewModel: PdfViewModel by viewModels<PdfViewModel> { PdfViewModelFactory(applicationContext, url, name) }
            pdfViewModel.getLoadedFile().observe(this) { uri ->
                findViewById<PDFView>(R.id.activity_main_pdf_view).fromFile(uri.toFile()).show()
            }
        }

        binding.backArrow.setOnClickListener {
            finish()
        }

    }
}