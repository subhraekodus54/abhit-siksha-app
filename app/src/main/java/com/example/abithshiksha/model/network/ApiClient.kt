package com.example.abithshiksha.model.network

import android.content.Context
import android.util.Log
import com.example.abithshiksha.BuildConfig
import com.user.caregiver.isConnectedToInternet
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

class ApiClient {

    private var mRetrofit: Retrofit? = null
    private var mRetrofitWithToken: Retrofit? = null
    private val mLoggingInterceptor = HttpLoggingInterceptor()


    /***
     * Without token
     */

    private val httpLogClient: OkHttpClient
        get() {
            if (BuildConfig.DEBUG) {
                mLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            }

            return OkHttpClient.Builder()
                .addInterceptor(mLoggingInterceptor)
                .connectTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .build()
        }

    val client: Retrofit
        get() {
            if (mRetrofit == null) {
                mRetrofit = Retrofit.Builder()
                    .baseUrl(ApiConstants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpLogClient)
                    .build()
            }
            return mRetrofit!!
        }


    /***
     * With token
     */

    private fun getHttpLogClientWithToken(context: Context): OkHttpClient {
        if (BuildConfig.DEBUG) {
            mLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .cache(getCache(context))
            .addInterceptor(mLoggingInterceptor)
            .addInterceptor(TokenRenewInterceptor(context))
            .addNetworkInterceptor(provideCacheInterceptor(context))
            .addInterceptor(provideOfflineCacheInterceptor(context))
            .connectTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()
    }

    fun getClientWithToken(context: Context): Retrofit {
        if (mRetrofitWithToken == null) {
            mRetrofitWithToken = Retrofit.Builder()
                .baseUrl(ApiConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(getHttpLogClientWithToken(context))
                .build()
        }
        return mRetrofitWithToken!!
    }

    private fun getCache(context: Context): Cache? {
        var cache: Cache? = null
        try {
            cache = Cache(File(context.cacheDir, "http-cache"), (80 * 1024 * 1024).toLong())
        } catch (e: java.lang.Exception) {
            Log.e("Cache", "Error in creating  Cache!")
        }

        return cache
    }

    private fun provideCacheInterceptor(context: Context): Interceptor {
        return Interceptor { chain ->
            val response = chain.proceed(chain.request())
            val cacheControl: CacheControl = if (context.isConnectedToInternet()) {
                CacheControl.Builder().maxAge(0, TimeUnit.SECONDS).build()
            } else {
                CacheControl.Builder()
                    .maxStale(8, TimeUnit.DAYS)
                    .build()
            }
            response.newBuilder()
                .removeHeader(HEADER_PRAGMA)
                .removeHeader(HEADER_CACHE_CONTROL)
                .header(HEADER_CACHE_CONTROL, cacheControl.toString())
                .build()
        }
    }

    private fun provideOfflineCacheInterceptor(context: Context): Interceptor {
        return Interceptor { chain ->
            var request = chain.request()
            if (!context.isConnectedToInternet()) {
                val cacheControl: CacheControl = CacheControl.Builder()
                    .maxStale(8, TimeUnit.DAYS)
                    .build()
                request = request.newBuilder()
                    .removeHeader(HEADER_PRAGMA)
                    .removeHeader(HEADER_CACHE_CONTROL)
                    .cacheControl(cacheControl)
                    .build()
            }
            chain.proceed(request)
        }
    }

    companion object {
        fun getInstance(context: Context): ApiInterface = ApiClient().getClientWithToken(context)
            .create(ApiInterface::class.java)

        const val HEADER_CACHE_CONTROL = "Cache-Control"
        const val HEADER_PRAGMA = "Pragma"
    }
}