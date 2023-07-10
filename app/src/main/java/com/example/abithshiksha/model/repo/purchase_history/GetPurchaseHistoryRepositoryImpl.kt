package com.example.abithshiksha.model.repo.purchase_history

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.abithshiksha.model.network.ApiClient
import com.example.abithshiksha.model.pojo.puchase_history.GetPurchaseHistoryResponse
import com.example.abithshiksha.model.repo.Outcome

class GetPurchaseHistoryRepositoryImpl (private val context: Context): GetPurchaseHistoryRepository {
    private val apiService = ApiClient.getInstance(context)

    override suspend fun purchaseHistory(token: String): Outcome<GetPurchaseHistoryResponse> {
        val apiResponse = MutableLiveData<Outcome<GetPurchaseHistoryResponse>>()
        try {
            val response = apiService.getPurchaseHistory(token)
            apiResponse.value = Outcome.success(response!!)
        } catch (e: Throwable) {
            apiResponse.value = Outcome.failure(e)
        }

        return apiResponse.value as Outcome<GetPurchaseHistoryResponse>
    }
}