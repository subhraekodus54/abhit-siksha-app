package com.example.abithshiksha.model.pojo.get_all_performance

data class Result(
    val all_subjects: List<AllSubject>,
    val mcq_performance: McqPerformance,
    val subject_progress: SubjectProgress,
    val time_spent: TimeSpent
)