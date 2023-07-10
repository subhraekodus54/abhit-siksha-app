package com.example.abithshiksha.model.pojo.get_videos

data class ResultX(
    val total_videos: Int,
    val user_id: String,
    val videos: List<Video>,
    val preview: Int
)