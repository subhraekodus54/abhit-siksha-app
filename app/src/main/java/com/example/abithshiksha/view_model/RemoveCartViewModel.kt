package com.example.abithshiksha.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.abithshiksha.model.repo.remove_cart_repo.RemoveCartRepository

class RemoveCartViewModel (private val mRemoveCartRepository: RemoveCartRepository): ViewModel() {
    fun removeCart(
        token: String,
        cart_id: Int
    ) = liveData {
        emit(mRemoveCartRepository.removeCart(token, cart_id))
    }
}