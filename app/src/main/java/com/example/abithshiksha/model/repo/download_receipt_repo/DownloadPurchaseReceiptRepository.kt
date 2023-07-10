package com.example.abithshiksha.model.repo.download_receipt_repo

import com.example.abithshiksha.model.pojo.get_purchase_receipt.GetPurchaseReceiptResponse
import com.example.abithshiksha.model.repo.Outcome

interface DownloadPurchaseReceiptRepository {
    suspend fun getReceipt(token: String, order_id: Int): Outcome<GetPurchaseReceiptResponse>
}