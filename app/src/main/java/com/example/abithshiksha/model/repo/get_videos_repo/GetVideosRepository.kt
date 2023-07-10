package com.example.abithshiksha.model.repo.get_videos_repo

import com.example.abithshiksha.model.pojo.get_videos.GetVideosResponse
import com.example.abithshiksha.model.repo.Outcome

interface GetVideosRepository {
    suspend fun getVideos(token: String, lesson_id: Int, page: Int): Outcome<GetVideosResponse>
}