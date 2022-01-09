package com.example.afreecatv_android_yoonseungil.src.dto

data class Repository(
    val incomplete_results: Boolean,
    val items: List<Item>,
    val total_count: Int
)