package com.example.abithshiksha.model.repo.get_topic_list_repo

import com.example.abithshiksha.model.pojo.get_review.GetReviewResponse
import com.example.abithshiksha.model.pojo.get_topic_list.GetTopicListResponse
import com.example.abithshiksha.model.repo.Outcome

interface GetTopicListRepository {
    suspend fun getTopicList(token: String, lesson_id: Int): Outcome<GetTopicListResponse>
}