package com.example.abithshiksha.model.repo.get_gallery_repo

import com.example.abithshiksha.model.pojo.get_gallery.GetGalleryResponse
import com.example.abithshiksha.model.repo.Outcome

interface GetGalleryRepository {
    suspend fun getGallery(token: String): Outcome<GetGalleryResponse>
}