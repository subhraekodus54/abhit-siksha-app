package com.example.abithshiksha.model.repo.get_selected_addons_repo

import com.example.abithshiksha.model.pojo.get_addons.GetSelectedAddonsResponse
import com.example.abithshiksha.model.repo.Outcome

interface GetSelectedAddOnsRepository {
    suspend fun getSelectedAddons(token: String): Outcome<GetSelectedAddonsResponse>
}