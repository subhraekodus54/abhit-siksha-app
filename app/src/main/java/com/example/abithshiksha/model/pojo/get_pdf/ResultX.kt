package com.example.abithshiksha.model.pojo.get_pdf

data class ResultX(
    val pdfs: List<Pdf>,
    val total_pdfs: Int,
    val preview: Int
)