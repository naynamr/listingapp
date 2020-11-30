package com.example.test.model

data class MovieJsonResponse(
    val page: DataClass
)

data class DataClass(
    val title: String,
    val total_content_items: String,
    val page_num: String,
    val page_size: String,
    val content_items: ContentObj
)

data class ContentObj(
    var content: ArrayList<ContentItem>
)

data class ContentItem(
    val name: String,
    val poster_image: String
)

