package com.manila.fasaldoctor.model

data class UserPost(
    // Identifier for the user who created the post
    val text: String,   // The text content of the post
    val imageUrl: String? = null, // URL to an image attached to the post (nullable)
    val timestamp: Long
)// Timestamp indicating when the post was created
{
    // No-argument constructor
    constructor() : this("", "",  0)
}