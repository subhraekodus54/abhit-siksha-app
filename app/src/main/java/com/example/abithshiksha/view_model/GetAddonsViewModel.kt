package com.example.abithshiksha.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.abithshiksha.model.repo.get_addons_repo.GetAddonsRepository

class GetAddonsViewModel(private val mGetAddonsRepository: GetAddonsRepository): ViewModel() {
    fun getAddons(
        token: String,
        board_id: Int,
        standard: String
    ) = liveData {
        emit(mGetAddonsRepository.getAddons(token, board_id, standard))
    }
}