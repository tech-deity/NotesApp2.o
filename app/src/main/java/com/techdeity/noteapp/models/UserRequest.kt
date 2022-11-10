package com.techdeity.noteapp.models

data class UserRequest(
    val email: String,
    val password: String,
    val username: String
)