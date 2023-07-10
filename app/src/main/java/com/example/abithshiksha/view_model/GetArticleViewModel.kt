package com.example.abithshiksha.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.abithshiksha.model.repo.get_article_repo.GetArticleRepository

class GetArticleViewModel(private val mGetArticleRepository: GetArticleRepository): ViewModel() {
    fun getArticle(
        token: String,
        topic_id: Int,
        page: Int
    ) = liveData {
        emit(mGetArticleRepository.getArticle(token,topic_id,page))
    }
}