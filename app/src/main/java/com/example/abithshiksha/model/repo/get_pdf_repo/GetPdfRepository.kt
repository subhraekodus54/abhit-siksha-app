package com.example.abithshiksha.model.repo.get_pdf_repo

import com.example.abithshiksha.model.pojo.get_pdf.GetPdfResponse
import com.example.abithshiksha.model.repo.Outcome

interface GetPdfRepository {
    suspend fun getPdf(token: String, lesson_id: Int, page: Int): Outcome<GetPdfResponse>
}