package com.manila.fasaldoctor.model

data class Comment(
    var email: String = "",
    var commentText: String = ""
)


data class UserPost(
    val email: String = "",
    val role: String = "",
    val uid: String? = null,
    val text: String = "",
    val imageUrl: String? = null,
    val timestamp: Long = 0,
    var comments: List<Comment> = emptyList(),
    var postId: String? = null// List of comments
) {
    // Secondary constructor with named parameters
    constructor() : this(
        email = "",
        role = "",
        uid = null,
        text = "",
        imageUrl = null,
        timestamp = 0,
        comments = emptyList()
    )
}
