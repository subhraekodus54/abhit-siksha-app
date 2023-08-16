package com.example.abithshiksha.model.repo.get_addons_repo

import com.example.abithshiksha.model.pojo.add_ons.GetAddonsResponse
import com.example.abithshiksha.model.repo.Outcome

interface GetAddonsRepository {
    suspend fun getAddons(token: String, board_id: Int, standard: String): Outcome<GetAddonsResponse>
}