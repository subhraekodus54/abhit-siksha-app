package com.example.abithshiksha.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.abithshiksha.model.repo.get_carts_repo.GetCartsRepository

class GetCartsViewModel(private val mGetCartsRepository: GetCartsRepository): ViewModel() {
    fun getCarts(
        token: String,
    ) = liveData {
        emit(mGetCartsRepository.getCart(token))
    }
}