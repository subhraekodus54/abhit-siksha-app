package com.example.abithshiksha.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.abithshiksha.model.repo.get_order_id_repo.GetOrderIdRepository

class GetOrderIdViewModel(private val mGetOrderIdRepository: GetOrderIdRepository): ViewModel() {
    fun getOrderId(
        token: String,
        cart_id: Int
    ) = liveData {
        emit(mGetOrderIdRepository.getOrderId(token, cart_id))
    }
}