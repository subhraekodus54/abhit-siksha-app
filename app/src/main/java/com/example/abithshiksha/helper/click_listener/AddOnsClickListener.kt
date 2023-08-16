package com.example.abithshiksha.helper.click_listener

import android.view.View

interface AddOnsClickListener {
    fun onClick(view: View, id: Int, isChecked: Boolean, price: Int)
}