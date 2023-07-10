package com.example.abithshiksha.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.abithshiksha.model.repo.get_cart_details_repo.GetCartDetailsRepository

class GetCartDetailsViewModel(private val mGetCartDetailsRepository: GetCartDetailsRepository): ViewModel() {
    fun getCartDetails(
        token: String,
        cart_id: Int
    ) = liveData {
        emit(mGetCartDetailsRepository.getCartDetails(token, cart_id))
    }
}