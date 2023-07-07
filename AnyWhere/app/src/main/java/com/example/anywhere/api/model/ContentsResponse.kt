package com.example.anywhere.api.model

data class ContentsResponse(
    val Heading: String,
    val AbstractURL: String,
    val AbstractSource: String,
    val RelatedTopics: List<RelatedTopicModel>
)
