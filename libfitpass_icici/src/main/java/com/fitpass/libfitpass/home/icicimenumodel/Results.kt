package com.fitpass.libfitpass.home.icicimenumodel

import kotlin.collections.List

data class Results(
    val copyright_string:String,
    val faq: Faq,
    val product_list: ArrayList<Product_List>,
    val user_details: UserDetails,
    val workoutlist: ArrayList<Workoutlist>,
    val view_all_workout: ViewAllWorkout,
    val slide_webview:String
)