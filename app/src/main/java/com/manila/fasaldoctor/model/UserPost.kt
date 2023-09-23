package com.manila.fasaldoctor.model

data class Comment(
    val com_id : String = "",
    var email: String = "",
    var commentText: String = "",
    var upvotes: Int = 0,
    var IsUpvoted: Boolean = true
)


data class UserPost(
    val email: String = "",
    val userImgUrl :String? = null,
    val role: String = "",
    val uid: String? = null,
    val description: String = "",
    val text: String = "",
    val imageUrl: String? = null,
    val timestamp: Long = 0,
    var comments: List<Comment> = emptyList(),
    var postId: String? = null// List of comments
) {
    // Secondary constructor with named parameters
    constructor() : this(
        email = "",
        userImgUrl = null,
        role = "",
        uid = null,
        description="",
        text = "",
        imageUrl = null,
        timestamp = 0,
        comments = emptyList(),
    )
}
