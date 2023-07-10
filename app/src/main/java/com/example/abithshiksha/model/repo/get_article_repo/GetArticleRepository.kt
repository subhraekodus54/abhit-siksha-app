package com.example.abithshiksha.model.repo.get_article_repo

import com.example.abithshiksha.model.pojo.get_article.GetArticleResponse
import com.example.abithshiksha.model.repo.Outcome

interface GetArticleRepository {
    suspend fun getArticle(token: String, topic_id: Int, page: Int): Outcome<GetArticleResponse>
}