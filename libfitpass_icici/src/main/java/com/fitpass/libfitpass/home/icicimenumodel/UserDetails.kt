package com.fitpass.libfitpass.home.icicimenumodel

data class UserDetails(
    val app_key: String,
    val fitpass_id: Long,
    val secret_key: String,
    val user_id: Int,
    val user_name: String
)