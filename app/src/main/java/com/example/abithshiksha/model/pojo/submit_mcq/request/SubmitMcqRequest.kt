package com.example.abithshiksha.model.pojo.submit_mcq.request

data class SubmitMcqRequest(
    val set_id: String,
    val start_time: String,
    val end_time: String,
    val answers: MutableList<Answer>,
)