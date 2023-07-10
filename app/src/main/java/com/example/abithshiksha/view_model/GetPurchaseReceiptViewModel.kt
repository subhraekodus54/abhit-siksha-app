package com.example.abithshiksha.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.abithshiksha.model.repo.download_receipt_repo.DownloadPurchaseReceiptRepository

class GetPurchaseReceiptViewModel (private val mDownloadPurchaseReceiptRepository: DownloadPurchaseReceiptRepository): ViewModel() {

    fun getReceipt(
        token: String,
        order_id: Int
    ) = liveData {
        emit(mDownloadPurchaseReceiptRepository.getReceipt(token, order_id))
    }
}