package com.fitpass.libfitpass.workout


import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.util.Log
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import com.fitpass.libfitpass.R
import com.fitpass.libfitpass.base.constants.ConfigConstants
import com.fitpass.libfitpass.base.http_client.CustomLoader
import com.fitpass.libfitpass.base.utilities.FitpassConfig
import com.fitpass.libfitpass.base.utilities.FitpassConfigUtil
import com.fitpass.libfitpass.base.utilities.FitpassLocationUtil
import com.fitpass.libfitpass.base.utilities.FitpassPrefrenceUtil
import com.fitpass.libfitpass.databinding.ActivityFitpassWorkOutBinding
import com.fitpass.libfitpass.home.FitpassDashboard
import com.fitpass.libfitpass.home.icicimenumodel.Workoutlist
import com.fitpass.libfitpass.scanqrcode.FitpassScanQrCodeActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject
import java.lang.Exception
import java.lang.reflect.Type


class FitpassWorkOutActivity : AppCompatActivity() {
    lateinit var binding: ActivityFitpassWorkOutBinding
    var upcomingUrl=""
    var completeUrl=""
    var PERMISSION_CODE: Int = 101
    var LOCATION_PERMISSION_CODE1: Int = 103
    var workoutData=""
    var whitelisturl=""
    var workoutList=ArrayList<Workoutlist>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_fitpass_work_out);
        getIntentData()
        setHeader()
        setPadding()
        onClick()
        binding.webview.setBackgroundColor(0);
        binding.webview.setWebViewClient(WebViewClientDemo(this, this,whitelisturl,workoutList));
        binding.webview.setWebChromeClient(WebChromeClientDemo(this));
        binding.webview.getSettings().setJavaScriptEnabled(true);
        binding.webview.getSettings().setDomStorageEnabled(true);
        showLoader()
        binding.webview.loadUrl(upcomingUrl);

    }
    fun getIntentData(){
        var viewallworkout=intent.getStringExtra("viewallworkout")!!
        var workoutListdata=intent.getStringExtra("workoutList")!!
        whitelisturl=intent.getStringExtra("whitelisturl")!!
        if(!workoutListdata.isNullOrEmpty()){
            val listType: Type = object : TypeToken<ArrayList<Workoutlist?>?>() {}.type
            workoutList = Gson().fromJson(workoutListdata, listType)

        }
        if(viewallworkout.isNotEmpty()){
            var jsonObject=JSONObject(viewallworkout)
            upcomingUrl=jsonObject.optString("upcoming_workout")
            completeUrl=jsonObject.optString("past_workout")

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
        binding.llTab.setPadding(paddingPixel, 0, paddingPixel, 0);
        binding.rlHeader.rlBack.setPadding(paddingPixel, paddingPixel1, 0, 0);
    }
    fun onClick() {
        binding.rlHeader.rlBack.setOnClickListener {
            onBackPressed()
        }
        binding.llUpcommig.setOnClickListener {
            val typeface1 = ResourcesCompat.getFont(this, R.font.roboto_bold)
            val typeface2 = ResourcesCompat.getFont(this, R.font.roboto_regular)
            binding.tvUpcomming.setTypeface(typeface1)
            binding.tvCompleted.setTypeface(typeface2)
            binding.tvUpcomming.setTextColor(resources.getColor(R.color.orangedark))
            binding.tvCompleted.setTextColor(resources.getColor(R.color.white))
            binding.viewUpcomming.visibility = View.VISIBLE
            binding.viewCompleted.visibility = View.INVISIBLE
            showLoader()
            binding.webview.loadUrl(upcomingUrl);
        }

        binding.llCompleted.setOnClickListener {
            val typeface1 = ResourcesCompat.getFont(this, R.font.roboto_bold)
            val typeface2 = ResourcesCompat.getFont(this, R.font.roboto_regular)
            binding.tvUpcomming.setTypeface(typeface2)
            binding.tvCompleted.setTypeface(typeface1)
            binding.tvUpcomming.setTextColor(resources.getColor(R.color.white))
            binding.tvCompleted.setTextColor(resources.getColor(R.color.orangedark))
            binding.viewUpcomming.visibility = View.INVISIBLE
            binding.viewCompleted.visibility = View.VISIBLE
            showLoader()
            binding.webview.loadUrl(completeUrl);
        }
    }
    fun showLoader(){
        CustomLoader.showLoaderDialog(this, this)
    }

    private class WebViewClientDemo(val context: Context, val activity: Activity, whitelistUrl1:String,var workoutList:ArrayList<Workoutlist>) : WebViewClient() {
        var whitelistUrl=""
        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            view!!.loadUrl(url!!);
            Log.d("url1", url);
            whitelistUrl=FitpassPrefrenceUtil.getStringPrefs(context,FitpassPrefrenceUtil.WHITE_LIST_URL,"").toString()
            Log.d("urlwhite", whitelistUrl );
            if(url.contains(whitelistUrl)){
                var urlslpit=url.split("=").toTypedArray()
                var userSheduleId=urlslpit[1]
                var workoutData=""
                (context as FitpassWorkOutActivity).updateWorkout("")
                for(data in workoutList){
                    if(data.user_schedule_id.equals(userSheduleId)){
                        workoutData=Gson().toJson(data)
                        break
                    }
                }
                (context as FitpassWorkOutActivity).updateWorkout(workoutData)
                (context as FitpassWorkOutActivity).checkPermission()
                view.stopLoading()
            }
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
        override fun onPageCommitVisible(view: WebView?, url: String?) {
            super.onPageCommitVisible(view, url)
           // Handler().postDelayed(Runnable {
                CustomLoader.hideLoaderDialog(activity)
           // },1000)
        }

    }

    private class WebChromeClientDemo(val activity: Activity) : WebChromeClient() {
        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            super.onProgressChanged(view, newProgress)

        }

    }
    fun updateWorkout(workoutData: String){
        this.workoutData=workoutData
    }

    fun checkPermission() {
        val manager: LocationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            gpsAlert(
                resources.getString(R.string.turnonlocation),
                resources.getString(R.string.locationmsg)
            )
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.CAMERA
                    ),
                    PERMISSION_CODE
                )
            } else if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ),
                    LOCATION_PERMISSION_CODE1
                )

            } else {
                openScanActivity()
            }
        } else {
            Log.d("checkPermission", "grant")
            openScanActivity()
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.d("onRequestResult", requestCode.toString() + "...")
        if (requestCode == PERMISSION_CODE) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED
            ) {
                alert(
                    resources.getString(R.string.turnoncamera),
                    resources.getString(R.string.cameramsg)
                )

            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("onRequestResult", "PERMISSION_GRANTED")
                checkPermission()

            }
        } else if (requestCode == LOCATION_PERMISSION_CODE1) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
                == PackageManager.PERMISSION_DENIED
            ) {
                alert(
                    resources.getString(R.string.turnonlocation),
                    resources.getString(R.string.locationmsg)
                )
            } else if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
                == PackageManager.PERMISSION_DENIED
            ) {
                alert(
                    resources.getString(R.string.turnonlocation),
                    resources.getString(R.string.locationmsg)
                )
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("onRequestResult", "PERMISSION_GRANTED")
                openScanActivity()

            }
        }

    }

    fun alert(title: String, msg: String) {
        binding.rlAlert.visibility = View.VISIBLE
        binding.alertpopup.tvTitle.setText(title)
        binding.alertpopup.tvMsg.setText(msg)
        binding.alertpopup.tvCancel.setOnClickListener {
            binding.rlAlert.visibility = View.GONE
        }
        binding.alertpopup.tvSetting.setOnClickListener {
            binding.rlAlert.visibility = View.GONE
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", this.packageName, null)
            intent.data = uri
            startActivity(intent)
        }

    }


    fun gpsAlert(title: String, msg: String) {
        binding.rlAlert.visibility = View.VISIBLE
        binding.alertpopup.tvTitle.setText(title)
        binding.alertpopup.tvMsg.setText(msg)
        binding.alertpopup.tvCancel.setOnClickListener {
            binding.rlAlert.visibility = View.GONE
        }
        binding.alertpopup.tvSetting.setOnClickListener {
            binding.rlAlert.visibility = View.GONE
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }
    }
    fun openScanActivity() {
        var intent= Intent(this, FitpassScanQrCodeActivity::class.java)
        if (!workoutData.isNullOrEmpty()) {
            intent.putExtra("workoutData", workoutData)
        }
        startActivity(intent)

    }
}