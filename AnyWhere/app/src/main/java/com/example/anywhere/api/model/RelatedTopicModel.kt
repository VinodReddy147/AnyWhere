package com.example.anywhere.api.model

data class RelatedTopicModel(
    val FirstURL: String,
    val Icon: Icon,
    var Result: String,
    val Text: String,
    var characterName: String = ""
)

data class Icon(
    val Height: String,
    val URL: String,
    val Width: String
)
