package com.fitpass.libfitpass.fitpasschallenge.listeners

interface FitpasssFragmentInteractionListener {
    fun onFragmentLoadingStart(fragmentCode:Int,fragmentName:String)
    fun onFragmentLoadComplete(fragmentCode:Int,fragmentName:String)
    fun onFragmentLoadFail(fragmentCode:Int,fragmentName:String)
}