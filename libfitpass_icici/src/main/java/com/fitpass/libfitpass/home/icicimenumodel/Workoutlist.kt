package com.fitpass.libfitpass.home.icicimenumodel

data class Workoutlist(
    val activity_id: String,
    val address: String,
    val date_of_workout: String,
    val end_time: Int,
    val latitude: String,
    val longitude: String,
    val ongoing_workout: Int,
    val security_code: String,
    val start_time: Int,
    val studio_id: String,
    val studio_logo: String,
    val studio_name: String,
    val urc_updated_time: Int,
    val user_schedule_id: String,
    val workout_id: String,
    val workout_name: String,
    val workout_status: String,
    val workout_time: String,
    val workout_time_id: String,
    val workout_label: String,
)