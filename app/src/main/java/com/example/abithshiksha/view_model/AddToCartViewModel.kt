package com.example.abithshiksha.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.abithshiksha.model.repo.add_to_cart_repo.AddToCartRepository

class AddToCartViewModel(private val mAddToCartRepository: AddToCartRepository): ViewModel() {
    fun addToCart(
        course_type: Int,
        subjects: List<Int>,
        is_buy: Int,
        addons: List<Int>,
        token: String
    ) = liveData {
        emit(mAddToCartRepository.addToCart(course_type, subjects, is_buy, addons, token))
    }
}