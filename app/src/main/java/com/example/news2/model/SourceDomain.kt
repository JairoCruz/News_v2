package com.example.news2.model

data class SourceDomain(
    var id: Long,
    val idSource: String,
    val name: String,
    val description: String,
    val url: String,
    val category: String,
    val language: String,
    val country: String,
    var isChecked: Boolean
)