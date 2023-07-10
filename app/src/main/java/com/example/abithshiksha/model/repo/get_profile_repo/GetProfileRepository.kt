package com.example.abithshiksha.model.repo.get_profile_repo

import com.example.abithshiksha.model.pojo.get_profile.GetProfileResponse
import com.example.abithshiksha.model.repo.Outcome

interface GetProfileRepository {
    suspend fun getProfile(token: String): Outcome<GetProfileResponse>
}