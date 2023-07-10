package com.example.abithshiksha.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.abithshiksha.model.repo.get_banner_repo.GetBannerRepository

class GetBannerViewModel (private val mGetBannerRepository: GetBannerRepository): ViewModel() {

    fun getBanner(
        token: String,
    ) = liveData {
        emit(mGetBannerRepository.getBanner(token))
    }
}