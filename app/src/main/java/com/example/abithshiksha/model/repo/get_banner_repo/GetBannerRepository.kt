package com.example.abithshiksha.model.repo.get_banner_repo

import com.example.abithshiksha.model.pojo.banner.GetBannerResponse
import com.example.abithshiksha.model.repo.Outcome

interface GetBannerRepository {
    suspend fun getBanner(token: String): Outcome<GetBannerResponse>
}