package com.example.abithshiksha.model.pojo.get_videos

data class Video(
    val id: Int,
    val original_video_path: String,
    val title: String,
    val video_size_480: String,
    val video_size_720: String,
    val video_thumbnail_image: String,
    val video_duration: String,
    val purchase: Int
)