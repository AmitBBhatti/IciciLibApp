package com.fitpass.libfitpass.home.listeners

import com.fitpass.libfitpass.home.icicimenumodel.Product_List
import com.fitpass.libfitpass.home.icicimenumodel.Workoutlist


interface FitpassiciciHomeListener {
    fun onMenuClick(data: Product_List)
    fun workOutClick(data: Workoutlist)
}