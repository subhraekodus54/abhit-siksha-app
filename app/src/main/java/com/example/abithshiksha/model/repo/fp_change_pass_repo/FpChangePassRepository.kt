package com.example.abithshiksha.model.repo.fp_change_pass_repo

import com.example.abithshiksha.model.pojo.fp_change_pass.FpChangePassResponse
import com.example.abithshiksha.model.repo.Outcome

interface FpChangePassRepository {
    suspend fun changePass(user_id: String,
                           password: String): Outcome<FpChangePassResponse>
}