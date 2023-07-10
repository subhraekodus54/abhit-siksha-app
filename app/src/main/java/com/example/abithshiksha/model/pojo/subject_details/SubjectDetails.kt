package com.example.abithshiksha.model.pojo.subject_details

data class SubjectDetails(
    val board_id: Int,
    val board_name: String,
    val class_id: Int,
    val class_name: String,
    val created_at: String,
    val description: String,
    val id: Int,
    val subject_amount: Int,
    val subject_name: String,
    val total_article: Int,
    val total_image_pdf: Int,
    val total_lesson: Int,
    val total_topic: Int,
    val total_video: Int,
    val why_learn: String,
    val rating: String,
    val already_purchase: Int,
    val already_incart: Int
)