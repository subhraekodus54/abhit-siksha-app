package com.example.abithshiksha.model.pojo.add_ons

data class AddOnsModel(
    val id: Int,
    val name: String,
    val price: Int,
    var isSelected: Boolean? = false
)
