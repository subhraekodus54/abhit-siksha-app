package com.example.abithshiksha

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.view.WindowManager
import appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.core.context.startKoin

class MyApplication : Application(){

    override fun onCreate() {
        super.onCreate()
        application = this
        startKoin {
            androidContext(this@MyApplication)
            androidFileProperties()
            modules(appModule)
        }

        setupActivityListener()
    }

    companion object{
        lateinit var application: Application
        fun getInstance(): Application{
            return application
        }
    }

    private fun setupActivityListener() {
        registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(p0: Activity, p1: Bundle?) {

                p0.getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
            }

            override fun onActivityStarted(p0: Activity) {

            }

            override fun onActivityResumed(p0: Activity) {

            }

            override fun onActivityPaused(p0: Activity) {

            }

            override fun onActivityStopped(p0: Activity) {

            }

            override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {

            }

            override fun onActivityDestroyed(p0: Activity) {

            }


        })
    }

}