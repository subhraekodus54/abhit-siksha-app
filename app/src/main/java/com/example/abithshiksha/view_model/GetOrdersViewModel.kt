package com.example.abithshiksha.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.abithshiksha.model.repo.get_orders_repo.GetOrdersRepository

class GetOrdersViewModel(private val mGetOrdersViewModel: GetOrdersRepository): ViewModel() {
    fun getOrder(
        token: String,
    ) = liveData {
        emit(mGetOrdersViewModel.getOrders(token))
    }
}