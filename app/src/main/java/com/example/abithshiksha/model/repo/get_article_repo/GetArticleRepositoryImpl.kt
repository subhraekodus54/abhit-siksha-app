package com.example.abithshiksha.model.repo.get_article_repo

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.abithshiksha.model.network.ApiClient
import com.example.abithshiksha.model.pojo.get_article.GetArticleResponse
import com.example.abithshiksha.model.repo.Outcome

class GetArticleRepositoryImpl(private val context: Context): GetArticleRepository {
    private val apiService = ApiClient.getInstance(context)
    override suspend fun getArticle(token: String, topic_id: Int, page: Int): Outcome<GetArticleResponse> {
        val apiResponse = MutableLiveData<Outcome<GetArticleResponse>>()
        try {
            val response = apiService.getArticle(token,topic_id,page)
            apiResponse.value = Outcome.success(response!!)
        } catch (e: Throwable) {
            apiResponse.value = Outcome.failure(e)
        }
        return apiResponse.value as Outcome<GetArticleResponse>
    }
}