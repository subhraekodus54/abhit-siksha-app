package com.example.abithshiksha.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.abithshiksha.model.repo.purchase_history.GetPurchaseHistoryRepository

class GetPurchaseHistoryViewModel (private val mGetPurchaseHistoryRepository: GetPurchaseHistoryRepository): ViewModel() {

    fun getPurchaseHistory(
        token: String,
    ) = liveData {
        emit(mGetPurchaseHistoryRepository.purchaseHistory(token))
    }
}