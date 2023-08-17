package com.example.abithshiksha.model.pojo.get_addons

data class Result(
    val code: Int,
    val message: String,
    val selected_addons: List<SelectedAddon>
)