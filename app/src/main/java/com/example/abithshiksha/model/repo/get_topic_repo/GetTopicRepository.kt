package com.example.abithshiksha.model.repo.get_topic_repo

import com.example.abithshiksha.model.pojo.get_topics.GetTopicResponse
import com.example.abithshiksha.model.repo.Outcome

interface GetTopicRepository {
    suspend fun getTopic(token: String, lesson_id: Int, page: Int): Outcome<GetTopicResponse>
}