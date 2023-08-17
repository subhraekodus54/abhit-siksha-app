package com.example.abithshiksha.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.abithshiksha.model.repo.get_selected_addons_repo.GetSelectedAddOnsRepository

class GetSelectedAddonViewModel (private val mGetSelectedAddOnsRepository: GetSelectedAddOnsRepository): ViewModel() {
    fun getSelectedAddons(
        token: String
    ) = liveData {
        emit(mGetSelectedAddOnsRepository.getSelectedAddons(token))
    }
}