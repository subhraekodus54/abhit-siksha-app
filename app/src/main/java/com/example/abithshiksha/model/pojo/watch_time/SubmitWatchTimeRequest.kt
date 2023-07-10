package com.example.abithshiksha.model.pojo.watch_time

data class SubmitWatchTimeRequest(
    val lesson_id: Int,
    val video_start_time: String,
    val video_end_time: String
)