package com.example.abithshiksha.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.abithshiksha.model.repo.get_gallery_repo.GetGalleryRepository

class GetGalleryViewModel(private val mGetGalleryRepository: GetGalleryRepository): ViewModel() {

    fun getGallery(
        token: String,
    ) = liveData {
        emit(mGetGalleryRepository.getGallery(token))
    }
}