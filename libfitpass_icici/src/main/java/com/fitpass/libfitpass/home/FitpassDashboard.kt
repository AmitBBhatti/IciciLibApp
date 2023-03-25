package com.fitpass.libfitpass.home

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
import android.util.Base64
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.fitpass.libfitpass.R
import com.fitpass.libfitpass.base.constants.ConfigConstants
import com.fitpass.libfitpass.base.http_client.CustomLoader
import com.fitpass.libfitpass.base.utilities.*
import com.fitpass.libfitpass.databinding.ActivityFitpassIciciBinding
import com.fitpass.libfitpass.home.http_client.CommonRepository
import com.fitpass.libfitpass.home.icicimenumodel.Product_List
import com.fitpass.libfitpass.home.icicimenumodel.Workoutlist
import com.fitpass.libfitpass.home.listeners.FitpassiciciHomeListener
import com.fitpass.libfitpass.home.viewmodel.FitpassIciciViewModel
import com.fitpass.libfitpass.home.viewmodelfactory.IciciHomeViewModelFactory
import com.fitpass.libfitpass.scanqrcode.FitpassScanQrCodeActivity
import com.fitpass.libfitpass.workout.FitpassWorkOutActivity
import com.google.gson.Gson
import org.json.JSONObject
class FitpassDashboard : AppCompatActivity(), View.OnClickListener ,FitpassiciciHomeListener{
    lateinit var binding:ActivityFitpassIciciBinding
    private var homeViewModel: FitpassIciciViewModel? = null
    var vendorId: String = ""
    var policyNo: String = ""
    var memberId: String = ""
    var weburl: String = ""
    var show_header: Boolean = true
    var PERMISSION_CODE: Int = 101
    var LOCATION_PERMISSION_CODE: Int = 102
    var LOCATION_PERMISSION_CODE1: Int = 103
    var LOCATION_PERMISSION_CODE2: Int = 104
    var MY_PERMISSIONS_REQUEST_ACTIVITY_RECOGNITION: Int = 105
    var workoutData=""
    private val activitiesConfig =
        "[{\"image_id\":\"activity_2\",\"activity_color\":\"#815E0D\",\"start_color\":\"#998046\",\"end_color\":\"#815E0D\",\"activity_id\":\"2\"}," +
                "{\"image_id\":\"activity_4\",\"activity_color\":\"#FBBB74\",\"start_color\":\"#F5BF40\",\"end_color\":\"#FBBB74\",\"activity_id\":\"4\"}," +
                "{\"image_id\":\"activity_15\",\"activity_color\":\"#6E1F7A\",\"start_color\":\"#AD68B7\",\"end_color\":\"#6E1F7A\",\"activity_id\":\"15\"}," +
                "{\"image_id\":\"activity_1\",\"activity_color\":\"#4DC349\",\"start_color\":\"#80DB7E\",\"end_color\":\"#4DC349\",\"activity_id\":\"1\"}," +
                "{\"image_id\":\"activity_6\",\"activity_color\":\"#6345C6\",\"start_color\":\"#9F6EDC\",\"end_color\":\"#6345C6\",\"activity_id\":\"6\"}," +
                "{\"image_id\":\"activity_14\",\"activity_color\":\"#59B1D7\",\"start_color\":\"#10BDDB\",\"end_color\":\"#59B1D7\",\"activity_id\":\"14\"}," +
                "{\"image_id\":\"activity_13\",\"activity_color\":\"#1C1162\",\"start_color\":\"#675EA4\",\"end_color\":\"#1C1162\",\"activity_id\":\"13\"}," +
                "{\"image_id\":\"activity_16\",\"activity_color\":\"#D04A31\",\"start_color\":\"#F5908C\",\"end_color\":\"#D04A31\",\"activity_id\":\"16\"}," +
                "{\"image_id\":\"activity_3\",\"activity_color\":\"#5E8CEB\",\"start_color\":\"#879AF5\",\"end_color\":\"#5E8CEB\",\"activity_id\":\"3\"}," +
                "{\"image_id\":\"activity_12\",\"activity_color\":\"#8A2E14\",\"start_color\":\"#A85D4A\",\"end_color\":\"#8A2E14\",\"activity_id\":\"12\"}," +
                "{\"image_id\":\"activity_9\",\"activity_color\":\"#41CCC6\",\"start_color\":\"#5FEAE5\",\"end_color\":\"#41CCC6\",\"activity_id\":\"9\"}," +
                "{\"image_id\":\"activity_10\",\"activity_color\":\"#C734E0\",\"start_color\":\"#E981FB\",\"end_color\":\"#C734E0\",\"activity_id\":\"10\"}," +
                "{\"image_id\":\"activity_11\",\"activity_color\":\"#8F40E6\",\"start_color\":\"#B47DF1\",\"end_color\":\"#8F40E6\",\"activity_id\":\"11\"}," +
                "{\"image_id\":\"activity_7\",\"activity_color\":\"#DA4361\",\"start_color\":\"#EF78BA\",\"end_color\":\"#DA4361\",\"activity_id\":\"7\"}," +
                "{\"image_id\":\"activity_8\",\"activity_color\":\"#EAC644\",\"start_color\":\"#EBE382\",\"end_color\":\"#EAC644\",\"activity_id\":\"8\"}," +
                "{\"image_id\":\"activity_5\",\"activity_color\":\"#D04A31\",\"start_color\":\"#F5908C\",\"end_color\":\"#D04A31\",\"activity_id\":\"5\"}," +
                "{\"image_id\":\"activity_17\",\"activity_color\":\"#DA4361\",\"start_color\":\"#EF78BA\",\"end_color\":\"#DA4361\",\"activity_id\":\"17\"}," +
                "{\"image_id\":\"activity_18\",\"activity_color\":\"#EAC644\",\"start_color\":\"#EBE382\",\"end_color\":\"#EAC644\",\"activity_id\":\"18\"}," +
                "{\"image_id\":\"activity_19\",\"activity_color\":\"#41CCC6\",\"start_color\":\"#5FEAE5\",\"end_color\":\"#41CCC6\",\"activity_id\":\"19\"}]"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= DataBindingUtil.setContentView(this,R.layout.activity_fitpass_icici);
        setHeader()
        setPadding()
        init()
        setWebViewHeight()
        FitpassPrefrenceUtil.setStringPrefs(
            this,
            FitpassPrefrenceUtil.ACTIVITY_DATA,
            activitiesConfig
        )
        binding.llDetail.visibility=View.GONE
        binding.llScanQr.visibility=View.GONE
        binding.webview.setBackgroundColor(0);
        var commonRepository = CommonRepository(this, this)
        var viewModelFactories = IciciHomeViewModelFactory(
            commonRepository,
            this,
            this,
            this
        )
        homeViewModel = ViewModelProvider(this, viewModelFactories!!).get(FitpassIciciViewModel::class.java)
        binding.lifecycleOwner = this
        binding.homedata = homeViewModel

        binding.webview.setWebViewClient(WebViewClientDemo(this,this,binding));
        binding.webview.setWebChromeClient(WebChromeClientDemo(this,binding));
        binding.webview.getSettings().setJavaScriptEnabled(true);
        binding.webview.getSettings().setDomStorageEnabled(true);

        homeViewModel!!.slide_webview.observe(this,{
            if(homeViewModel!!.slide_webview.value!!.isNotEmpty()){
                binding.webview.loadUrl(homeViewModel!!.slide_webview.value!!);
            }
        })


        if(getIntentData()) {
            getHomeData()
        }


    }
    fun init(){
        binding.rlHeader.rlBack.setOnClickListener(this);
        binding.llScanQr.setOnClickListener(this);
        binding.tvViewMore.setOnClickListener(this);
    }
    fun setWebViewHeight(){
        val displayMetrics = DisplayMetrics()
        windowManager.getDefaultDisplay().getMetrics(displayMetrics)
        val height: Int = displayMetrics.heightPixels
        var width = displayMetrics.widthPixels
        var density=displayMetrics.scaledDensity
        Log.d("width",width.toString())
       var halfwidth=width/2
        binding.webview.layoutParams.height=(halfwidth+(24*density)).toInt()
    }
    fun getIntentData(): Boolean {
        var bundle = intent.extras
        if (bundle != null) {
            if (!bundle.containsKey("vendor_id")) {
                FitpassToastUtil.showToastLong(this, resources.getString(R.string.vendormsg))
                onBackPressed()
                return false
            } else if (!bundle.containsKey("policy_number")) {
                FitpassToastUtil.showToastLong(this, resources.getString(R.string.policymsg))
                onBackPressed()
                return false
            } else if (!bundle.containsKey("member_id")) {
                FitpassToastUtil.showToastLong(this, resources.getString(R.string.membermsg))
                onBackPressed()
                return false
            } else {
                vendorId = bundle.getString("vendor_id")!!
                policyNo = bundle.getString("policy_number")!!
                memberId = bundle.getString("member_id")!!
                return true
            }
        } else {
            FitpassToastUtil.showToastLong(this, resources.getString(R.string.vendormsg))
            onBackPressed()
            return false
        }
    }

    fun getHomeData()
    {
        homeViewModel?.let {
            var request = JSONObject()
            request.put("vendor_id", vendorId.toInt())
            request.put("policy_number", policyNo)
            request.put("member_id", memberId)
            if (FitpassNetworkUtil.checkInternetConnection(this)) {
                CustomLoader.showLoaderDialog(this, this)
                //var request=IciciHomeRequest(memberId,policyNo,vendorId.toInt())
                it.getHomeData(request)
            }

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
        binding.llApiDetail.setPadding(paddingPixel, 0, paddingPixel, 0);
        binding.rlHeader.rlBack.setPadding(paddingPixel, paddingPixel1, 0, 0);
    }

    override fun onClick(view: View) {
        when (view.getId()) {
            R.id.rlBack -> onBackPressed()
            R.id.llScanQr -> {
                workoutData=""
               checkPermission()
            }
            R.id.tvViewMore->
            {
                var intent=Intent(this,FitpassWorkOutActivity::class.java)
                intent.putExtra("viewallworkout",homeViewModel!!.viewallworkout.value)
                intent.putExtra("workoutList",Gson().toJson(homeViewModel!!.workoutList.value))
                intent.putExtra("whitelisturl",Gson().toJson(homeViewModel!!.whiteListUrl.value))
                startActivity(intent)
            }

        }
    }
    override fun onBackPressed() {
        if (binding.webview.canGoBack()) {
            binding.webview.goBack()
        } else {
            super.onBackPressed()
        }
    }
    fun updateWorkout(workoutData: String){
        this.workoutData=workoutData
    }
    private class WebViewClientDemo(context1: Context, activity1: Activity,val binding:ActivityFitpassIciciBinding) : WebViewClient() {
        var context: Context =context1
        var activity: Activity =activity1
        var whitelistUrl=""
        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            view!!.loadUrl(url!!);
            Log.d("url1",url+"..");
            whitelistUrl=FitpassPrefrenceUtil.getStringPrefs(context,FitpassPrefrenceUtil.WHITE_LIST_URL,"").toString()
            Log.d("urlwhite", whitelistUrl );
            if(url.contains(whitelistUrl)){
                var urlslpit=url.split("=").toTypedArray()
                var userSheduleId=urlslpit[1]
                var workoutData=""
                (context as FitpassDashboard).updateWorkout("")
                for(data in  (context as FitpassDashboard)!!.homeViewModel!!.workoutList.value!!){
                    if(data.user_schedule_id.equals(userSheduleId)){
                        workoutData=Gson().toJson(data)
                        break
                    }
                }
                (context as FitpassDashboard).updateWorkout(workoutData)
                (context as FitpassDashboard).checkPermission()
                view.stopLoading()
            }
            return true
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            Log.d("url2",url+"..");
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            Log.d("url3",url+"..");

        }

        override fun onPageCommitVisible(view: WebView?, url: String?) {
            super.onPageCommitVisible(view, url)
            Log.d("url4",url+"..");
            binding.llDetail.visibility=View.VISIBLE
            binding.llApiDetail.visibility=View.VISIBLE
            //binding.llScanQr.visibility=View.VISIBLE
            CustomLoader.hideLoaderDialog(activity)
           /* Handler().postDelayed(Runnable {
                binding.llDetail.visibility=View.VISIBLE
            },1000)
            Handler().postDelayed(Runnable {
                binding.llApiDetail.visibility=View.VISIBLE
               // binding.llScanQr.visibility=View.VISIBLE
                CustomLoader.hideLoaderDialog(activity)
            },2000)*/
        }

    }
    private class WebChromeClientDemo( val activity: Activity,val binding:ActivityFitpassIciciBinding) : WebChromeClient() {
        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            super.onProgressChanged(view, newProgress)
            Log.d("newProgress",newProgress.toString())
        }

    }

    override fun onMenuClick(data: Product_List) {
        weburl = data.redircet_url
        this.show_header = data.show_header
        if (data.location_permission) {
            checkLocationPermission(true)
        } else {
            openWebActivity()
        }

    }

    override fun workOutClick(data: Workoutlist) {
         workoutData=Gson().toJson(data)
         checkPermission()
    }

    fun openWebActivity() {
        var intent = Intent(this, FitpassWebViewActivity::class.java)
        intent.putExtra("url", weburl)
        intent.putExtra("show_header", show_header)
        startActivity(intent)
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


    fun checkLocationPermission(isopenWeb: Boolean) {
        val manager: LocationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            gpsAlert(
                resources.getString(R.string.turnonlocation),
                resources.getString(R.string.locationmsg)
            )
        } else  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                if (isopenWeb) {

                    requestPermissions(
                        arrayOf(
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ),
                        LOCATION_PERMISSION_CODE
                    )
                }

            }else{
                FitpassLocationUtil.refreshLocation(this)
                if (isopenWeb) {
                    encodeBase64Url(false)
                }
            }
        }else {
                FitpassLocationUtil.refreshLocation(this)
                if (isopenWeb) {
                    encodeBase64Url(false)
                }
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
        } else if (requestCode == LOCATION_PERMISSION_CODE) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_DENIED
            ) {
                alert(
                    resources.getString(R.string.turnonlocation),
                    resources.getString(R.string.locationmsg)
                )
            } else if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_DENIED
            ) {
                alert(
                    resources.getString(R.string.turnonlocation),
                    resources.getString(R.string.locationmsg)
                )
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("onRequestResult", "PERMISSION_GRANTED")
                FitpassLocationUtil.refreshLocation(this)
                encodeBase64Url(true)
            }
        }else  if (requestCode == LOCATION_PERMISSION_CODE2) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_DENIED
            ) {
                alert(
                    resources.getString(R.string.turnonlocation),
                    resources.getString(R.string.locationmsg)
                )
            } else if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_DENIED
            ) {
                alert(
                    resources.getString(R.string.turnonlocation),
                    resources.getString(R.string.locationmsg)
                )
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                FitpassLocationUtil.refreshLocation(this)
               // openMap(true)
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

    fun encodeBase64Url(isHold: Boolean) {
        /*https://fitpass-pwa.web.app?querystring=latitude===28.6456&&longitude===77.2024&&header_font_color===#000000&&header_bgcolor===#ffffff&&user_id=1*/
        if (isHold) {
            Handler().postDelayed(Runnable {
                genBase64WithUrl()
                openWebActivity()
            }, 3000)
        } else {
            genBase64WithUrl()
            openWebActivity()
        }
    }
    fun genBase64WithUrl() {
        var fitpassConfig = FitpassConfig.getInstance()
        Log.d("latitude->",FitpassPrefrenceUtil.getStringPrefs(this, FitpassPrefrenceUtil.LATITUDE, "0.0").toString())
       /* var concaturl: String = "latitude===" + FitpassPrefrenceUtil.getStringPrefs(this, FitpassPrefrenceUtil.LATITUDE,
            "0.0"
        ) + "&&&longitude===" + FitpassPrefrenceUtil.getStringPrefs(this, FitpassPrefrenceUtil.LONGITUDE,
            "0.0") +
                "&&&header_font_color===" + fitpassConfig!!.getHeaderFontColor() +
                "&&&header_bgcolor===" + fitpassConfig.getHeaderColor() +
                "&&&user_id===" + FitpassPrefrenceUtil.getStringPrefs(
            this,
            FitpassPrefrenceUtil.USER_ID,
            ""
        ) + "&&&padding===" + fitpassConfig!!.getPadding()*/
        var concaturl: String = "latitude=" + FitpassPrefrenceUtil.getStringPrefs(this, FitpassPrefrenceUtil.LATITUDE,
            "0.0"
        ) + "&longitude=" + FitpassPrefrenceUtil.getStringPrefs(this, FitpassPrefrenceUtil.LONGITUDE,
            "0.0")
        Log.d("weburl", weburl)
        val data1: ByteArray = concaturl.encodeToByteArray()
        val base64: String = Base64.encodeToString(data1, Base64.DEFAULT)
       // weburl = weburl + "?querystring=" + base64
        weburl = weburl + "&"+concaturl
        Log.d("weburl", weburl)
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
            startActivity(Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }
    }
    fun openScanActivity() {
        FitpassLocationUtil.refreshLocation(this)
        var intent=Intent(this,FitpassScanQrCodeActivity::class.java)
        if (!workoutData.isNullOrEmpty()) {
            intent.putExtra("workoutData", workoutData)
        }
        startActivity(intent)
    }
}
