package com.example.abithshiksha.model.repo.edit_profile_repo

import com.example.abithshiksha.model.pojo.edit_profile.EditProfileResponse
import com.example.abithshiksha.model.repo.Outcome

interface EditProfileRepository {
    suspend fun editProfile(education: String?,
                            gender: String?,
                            address: String?,
                            token: String): Outcome<EditProfileResponse>
}