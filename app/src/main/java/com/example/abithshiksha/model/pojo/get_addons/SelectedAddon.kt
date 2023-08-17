package com.example.abithshiksha.model.pojo.get_addons

data class SelectedAddon(
    val addon_id: Int,
    val cart_id: Int,
    val created_at: String,
    val deleted_at: Any,
    val id: Int,
    val order_id: Int,
    val payment_status: String,
    val selected_addon: SelectedAddonX,
    val updated_at: String,
    val user_id: Int
)