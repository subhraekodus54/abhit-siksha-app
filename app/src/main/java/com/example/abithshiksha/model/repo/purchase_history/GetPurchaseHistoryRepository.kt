package com.example.abithshiksha.model.repo.purchase_history

import com.example.abithshiksha.model.pojo.puchase_history.GetPurchaseHistoryResponse
import com.example.abithshiksha.model.repo.Outcome

interface GetPurchaseHistoryRepository {
    suspend fun purchaseHistory(token: String): Outcome<GetPurchaseHistoryResponse>
}