package com.example.abithshiksha.model.repo.download_receipt_repo

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.abithshiksha.model.network.ApiClient
import com.example.abithshiksha.model.pojo.get_purchase_receipt.GetPurchaseReceiptResponse
import com.example.abithshiksha.model.repo.Outcome

class DownloadPurchaseReceiptRepositoryImpl (private val context: Context):
    DownloadPurchaseReceiptRepository {
    private val apiService = ApiClient.getInstance(context)

    override suspend fun getReceipt(
        token: String,
        order_id: Int
    ): Outcome<GetPurchaseReceiptResponse> {
        val apiResponse = MutableLiveData<Outcome<GetPurchaseReceiptResponse>>()
        try {
            val response = apiService.getPurchaseReceipt(token, order_id)
            apiResponse.value = Outcome.success(response!!)
        } catch (e: Throwable) {
            apiResponse.value = Outcome.failure(e)
        }
        return apiResponse.value as Outcome<GetPurchaseReceiptResponse>
    }
}