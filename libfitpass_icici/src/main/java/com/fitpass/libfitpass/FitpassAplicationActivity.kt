package com.fitpass.libfitpass

import android.app.Application

import android.util.Log
import androidx.multidex.MultiDex
import com.webengage.sdk.android.WebEngageActivityLifeCycleCallbacks
import com.webengage.sdk.android.WebEngageConfig

class FitpassAplicationActivity:Application() {
    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)
        Log.d("WebEngageConfig", "WebEngageConfig")
        val webEngageConfig = WebEngageConfig.Builder()
            .setWebEngageKey("in~11b56432b")
            .setDebugMode(true) // only in development mode
            .build()
        registerActivityLifecycleCallbacks(
            WebEngageActivityLifeCycleCallbacks(
                this,
                webEngageConfig
            )
        )
    }

}