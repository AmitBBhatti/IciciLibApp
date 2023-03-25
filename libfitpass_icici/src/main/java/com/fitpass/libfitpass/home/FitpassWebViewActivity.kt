package com.fitpass.libfitpass.home

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.databinding.DataBindingUtil
import com.fitpass.libfitpass.R
import com.fitpass.libfitpass.base.http_client.CustomLoader
import com.fitpass.libfitpass.base.utilities.FitpassConfig
import com.fitpass.libfitpass.base.utilities.FitpassConfigUtil
import com.fitpass.libfitpass.databinding.ActivityFitpassWebView2Binding
import com.fitpass.libfitpass.workout.FitpassWorkOutActivity
import java.lang.Exception

class FitpassWebViewActivity : AppCompatActivity() {
    lateinit var binding:ActivityFitpassWebView2Binding
    var url=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fitpass_web_view2)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_fitpass_web_view2);
        url=intent.getStringExtra("url")!!
        setHeader()
        setPadding()
        onClick()
        binding.webview.setBackgroundColor(0);
        binding.webview.setWebViewClient(WebViewClientDemo(this, this));
        binding.webview.setWebChromeClient(WebChromeClientDemo(this));
        binding.webview.getSettings().setJavaScriptEnabled(true);
        binding.webview.getSettings().setDomStorageEnabled(true);
        showLoader()
        binding.webview.loadUrl(url);
    }
    fun onClick(){
        binding.rlHeader.rlBack.setOnClickListener {
            onBackPressed()
        }
    }

    fun setHeader() {
        var fitpassConfig = FitpassConfig.getInstance()
        if (!fitpassConfig!!.getHeaderColor().isNullOrEmpty()) {
            FitpassConfigUtil.setStatusBarColor(this, this, fitpassConfig.getHeaderColor())

        }
        if (!fitpassConfig!!.getHeaderTitle().isNullOrEmpty()) {
            binding.rlHeader.tvHeader.setText(fitpassConfig!!.getHeaderTitle())
        }
        if (!fitpassConfig!!.getHeaderFontColor().isNullOrEmpty()) {
            try {
                binding.rlHeader.tvHeader.setTextColor(Color.parseColor(fitpassConfig!!.getHeaderFontColor()))
            }catch (e: Exception){

            }
        }
    }
    fun setPadding() {
        var fitpassConfig = FitpassConfig.getInstance()
        var paddingDp: Int = fitpassConfig!!.getPadding();
        var density = getResources().getDisplayMetrics().density.toFloat()
        var paddingPixel = (paddingDp * density).toInt();
        var paddingPixel1 = (22 * density).toInt();
        //binding.llDetail.setPadding(paddingPixel, 0, paddingPixel, 0);
        binding.rlHeader.rlBack.setPadding(paddingPixel, paddingPixel1, 0, 0);
    }

    fun showLoader(){
        CustomLoader.showLoaderDialog(this, this)
    }

    private class WebViewClientDemo(context1: Context, activity1: Activity) : WebViewClient() {
        var context: Context = context1
        var activity: Activity = activity1

        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            view!!.loadUrl(url!!);
            Log.d("url1", url + "..");
            return true
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)

            Log.d("url2", url + "..");

        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            Log.d("url3", url + "..");

        }

    }

    private class WebChromeClientDemo(val activity: Activity) : WebChromeClient() {
        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            super.onProgressChanged(view, newProgress)
            if (newProgress == 100) {
                CustomLoader.hideLoaderDialog(activity)

            }
        }

    }
    override fun onBackPressed() {
        if (binding.webview.canGoBack()) {
            binding.webview.goBack();
        } else {
            super.onBackPressed();
        }
    }
}