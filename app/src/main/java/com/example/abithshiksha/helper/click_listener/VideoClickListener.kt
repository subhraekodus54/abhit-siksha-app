package com.example.abithshiksha.helper.click_listener

import android.view.View

interface VideoClickListener {
    fun onClick(view: View, id: Int, url: String, url_480: String, url_720: String)
}