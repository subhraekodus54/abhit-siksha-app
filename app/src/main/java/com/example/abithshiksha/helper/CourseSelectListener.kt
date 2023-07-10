package com.example.abithshiksha.helper

import android.view.View

interface CourseSelectListener {
    fun onSelect(view: View, price: Int, isSelected: Boolean, id: Int)
}