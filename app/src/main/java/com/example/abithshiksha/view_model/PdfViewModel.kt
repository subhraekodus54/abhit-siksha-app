package com.example.abithshiksha.view_model

import android.content.Context
import android.net.Uri
import android.os.Handler
import android.os.Looper
import androidx.annotation.MainThread
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

/**
 * Provides path to a file when it's downloading and surviving view lifecycle
 *
 * @author Dmitry Borodin on 7/17/20.
 */

private var REMOTE_PDF_URL: String = ""
private var PDF_CACHED_FILE_NAME: String = ""

class PdfViewModel(private val cacheDir: File, private val url: String, private val name: String) : ViewModel() {

	/**
	 * Used to avoid starting another download while first one is in progress. It shouldn't happen anyway,
	 * but I would like to be sure, even after modification.
	 *
	 * If downloading with coroutines we can just keep reference to Job and check if it's active, then we won't need to set it to false.
	 */
	private var inProgress = false //should be used from main thread only

	private val pdfPath: MutableLiveData<Uri> = MutableLiveData<Uri>()

	init {
		loadPdf(url, name)
	}

	fun getLoadedFile(): LiveData<Uri> {
		return pdfPath
	}

	/**
	 * No synchronization is used for local variables since we call it only from main thread anyway
	 */
	@MainThread
	private fun loadPdf(url: String, name: String) {
		REMOTE_PDF_URL = url
		PDF_CACHED_FILE_NAME = name

		//if currently saving file to disk - Livedata will be updated later, just wait
		if (inProgress) return
		inProgress = true

		val pdf = File(cacheDir, PDF_CACHED_FILE_NAME)
		if (pdf.exists() && pdf.canRead()) {
			//file already in a cache - just show it
			pdfPath.value = pdf.toUri()
			inProgress = false
			return
		}

//		If your pdf may be changed and backend controling it - http mechanics are recommeneded for caching
//		This will cause additional network traffic and additional alignment with backend reqired to make sure proper http headers setup on a backend for OkHTTP cache to work properly
//		Then always get response from OkHttp after initialization
		// 10Mb - make sure PDf will fit
//		val cacheSize = 10L * 1024 * 1024
		// probably don't use default cache folder to not mix this cache with your usual REST responses cache - this file may not be requestet often, but it's big
//		val cacheDirectory = File(cacheDir.toURI())
//		val cache = Cache(cacheDirectory, cacheSize)
//		val client = OkHttpClient.Builder()
//				.cache(cache)
//				.build()

		val client = OkHttpClient.Builder().build()
		val request = Request.Builder().url(REMOTE_PDF_URL).build()
		client.newCall(request).enqueue(object : Callback {
			override fun onFailure(call: Call, e: IOException) {
				//show error state and send analytics report?
				Handler(Looper.getMainLooper()).post {
					inProgress = false
				}

			}

			override fun onResponse(call: Call, response: Response) {
				if (!response.isSuccessful) {
					//show error state and send analytics report?
					Handler(Looper.getMainLooper()).post {
						inProgress = false
					}
					return
				}
				val result = File(cacheDir, PDF_CACHED_FILE_NAME)
				val body = response.body!!
				val inputStream = body.byteStream()
				val input = BufferedInputStream(inputStream)
				val output: OutputStream = FileOutputStream(result)
				input.copyTo(output)
				output.flush();
				output.close();
				input.close(); //will closing just body is enough??
				body.close();

				//Update
				Handler(Looper.getMainLooper()).post {
					pdfPath.value = result.toUri()
					inProgress = false
				}
			}
		})
	}
}

class PdfViewModelFactory(private val appContext: Context, private val url: String, private val name: String) : ViewModelProvider.NewInstanceFactory() {
	//override fun <T : ViewModel?> create(modelClass: Class<T>): T = PdfViewModel(appContext.cacheDir, url, name) as T
	override fun <T : ViewModel> create(modelClass: Class<T>): T = PdfViewModel(appContext.cacheDir, url, name) as T
}