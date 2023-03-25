package com.fitpass.libfitpass.home.viewmodel

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.viewpager.widget.ViewPager
import com.fitpass.libfitpass.R
import com.fitpass.libfitpass.base.constants.ConfigConstants
import com.fitpass.libfitpass.base.customview.CustomToastView
import com.fitpass.libfitpass.base.dataencription.RandomKeyGenrator
import com.fitpass.libfitpass.base.fitpasscomparators.FitpassLowtoHighComparator
import com.fitpass.libfitpass.base.http_client.CustomLoader
import com.fitpass.libfitpass.base.utilities.FitpassHealthManager
import com.fitpass.libfitpass.base.utilities.FitpassPrefrenceUtil
import com.fitpass.libfitpass.home.FitpassWebViewActivityOld
import com.fitpass.libfitpass.home.http_client.ApiConstants
import com.fitpass.libfitpass.home.http_client.CommonRepository
import com.fitpass.libfitpass.home.http_client.HandleResponseListeners
import com.fitpass.libfitpass.home.listeners.FitpassHomeListener
import com.fitpass.libfitpass.home.models.*
import com.fitpass.libfitpass.home.models.List
import com.fitpass.libfitpass.scanqrcode.FitpassScanQrCodeActivity
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Scope
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.data.DataSet
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.data.Field
import com.google.android.gms.fitness.result.DataReadResult
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.Response
import java.text.DateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class HomeViewModel(
    val commonRepository: CommonRepository,
    val context: Context,
    val activity: Activity,
    val vpUpcomming: ViewPager,
    val llDots: LinearLayout,
    val fitpassHomeListener: FitpassHomeListener
) : ViewModel(), HandleResponseListeners {
    var homeresponse = MutableLiveData<HomeResponse>()
    var productList = MutableLiveData<ArrayList<Product>>()
    var faqList = MutableLiveData<ArrayList<List>>()
    var sliderList = MutableLiveData<ArrayList<SliderActivity>>()
    var macroList = MutableLiveData<ArrayList<MacrosDetail>>()
    private var handleResponseListeners: HandleResponseListeners? = null
    var homeViewModel: HomeViewModel? = null
    var isScanQrCode = MutableLiveData<Boolean>()
    var mClient: GoogleApiClient? = null
    init {
        isScanQrCode.value = false
        homeViewModel = this
        handleResponseListeners = this@HomeViewModel
    }

    fun getHomeData(requestBody: JSONObject) {
        viewModelScope.launch(Dispatchers.Main)
        {
            val dynamicSecretKey = RandomKeyGenrator.generate16DigitRandom()
           // val EncryptBodyKey = RandomKeyGenrator.getAlphaNumericString(16)
            val EncryptBodyKey = RandomKeyGenrator.getAlphaNumericString(16)
            RandomKeyGenrator.setRandomKey(dynamicSecretKey)
            val encryptedData = RandomKeyGenrator.encrptBodydataWithRandomKey(requestBody.toString())
            withContext(Dispatchers.Main)
            {
                CustomLoader.showLoaderDialog(activity, context)
            }
            async(Dispatchers.IO) {
                commonRepository.getHomeData(
                    ApiConstants.HOME_API,
                    encryptedData,
                    3,
                    handleResponseListeners!!
                )
            }.await()

        }

    }

    override fun handleSuccess(response1: Response<JsonObject?>, api: String?) {
        val jsonObject = JSONObject(response1!!.body().toString())
        Log.d("homeResponse", jsonObject.toString())
        var gson = Gson()
        var response: HomeResponse = gson.fromJson(jsonObject.toString(), HomeResponse::class.java)
        viewModelScope.launch {
            homeresponse.value = response
            if (response.results.slider_activity != null) {
                FitpassPrefrenceUtil.setStringPrefs(context, FitpassPrefrenceUtil.APP_KEY, homeresponse!!.value!!.results.user_details.app_key.toString())
                FitpassPrefrenceUtil.setStringPrefs(context, FitpassPrefrenceUtil.USER_ID, homeresponse!!.value!!.results.user_details.user_id.toString())
                FitpassPrefrenceUtil.setStringPrefs(context, FitpassPrefrenceUtil.SECRET_KEY, homeresponse!!.value!!.results.user_details.secret_key.toString())
                val slideractivitylist = ArrayList<SliderActivity>()
                for (data in response.results.slider_activity) {
                    if (data.action.equals(ConfigConstants.WORKOUT_ACTION)) {
                        isScanQrCode.value = true
                        slideractivitylist.add(data)
                    } else if (data.action.equals(ConfigConstants.NOTICE_ACTION)) {
                        if (!data?.data?.img.isNullOrEmpty()) {
                            slideractivitylist.add(data)
                        }

                    } else if (data.action.equals(ConfigConstants.MEAL_LOG_ACTION)) {
                        slideractivitylist.add(data)
                        macroList.value = data.macros_details

                    } else if (data.action.equals(ConfigConstants.HRA_COMPLETE_ACTION)) {
                        slideractivitylist.add(data)
                        if (!data?.data?.img.isNullOrEmpty()) {
                            slideractivitylist.add(data)
                        }
                    }
                }
                Collections.sort(slideractivitylist, FitpassLowtoHighComparator())
                if(sliderList.value!=null){
                    sliderList.value!!.clear()
                }
                sliderList.value = slideractivitylist
                setupPagerIndidcatorDots(0)
                setViewPagerListener()
            }
            if (response.results.product_list != null) {
                productList.value = response.results.product_list
            }
            if (response.results.faq.list != null) {
                faqList.value = response.results.faq.list
            }
            CustomLoader.hideLoaderDialog(activity)
        }
    }

    override fun handleErrorMessage(response: String?, api: String?) {
        Log.d("handleErrorMessage", response.toString())
        CustomToastView.errorToasMessage(activity, context, response)
        CustomLoader.hideLoaderDialog(activity)

    }

    fun setViewPagerListener() {
        vpUpcomming.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                
            }
            override fun onPageSelected(position: Int) {
                setupPagerIndidcatorDots(position)
            }
        })
        setupPagerIndidcatorDots(0)
    }

    private fun setupPagerIndidcatorDots(selectedPos: Int) {
        llDots.removeAllViews()
        var density = context.getResources().getDisplayMetrics().density.toFloat()
        var paddingPixel4 = (4 * density).toInt();
        var paddingPixel46 = (46 * density).toInt();
        var paddingPixel5 = (2 * density).toInt();
        for (i in 0 until sliderList.value!!.size) {
            var imageView = ImageView(context)
            val params1: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                paddingPixel4, paddingPixel4
            )
            params1.setMargins(paddingPixel5, 0, paddingPixel5, 0)
            val params2: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                paddingPixel46, paddingPixel4
            )
            params2.setMargins(paddingPixel5, 0, paddingPixel5, 0)
            if (i == selectedPos) {
                imageView.layoutParams = params2
                imageView.setBackground(context.getResources().getDrawable(R.drawable.gray_react))
            } else {
                imageView.layoutParams = params1
                imageView.setBackground(context.getResources().getDrawable(R.drawable.gray_circle))
            }
            llDots.addView(imageView)
            //binding.llDots.bringToFront()
        }
    }

    fun upCommingActions(action: String, url: String?,showHeader:Boolean?) {
        openWebActivity(url,showHeader)
    }

    fun openWebActivity(url: String?,showHeader:Boolean?) {
        if (!url.isNullOrEmpty()) {
            var intent = Intent(context, FitpassWebViewActivityOld::class.java)
            intent.putExtra("url", url)
            intent.putExtra("show_header", showHeader)
            context.startActivity(intent)
        }
    }


    fun openScanActivity() {
        var intent = Intent(context, FitpassScanQrCodeActivity::class.java)
        context.startActivity(intent)
    }

    public fun buildFitnessClient() {
        try {

            //if (mClient == null) {
                //  Log.d("GOOGLE_FIT_build", "connected")
                mClient = GoogleApiClient.Builder(context)
                    .addApi(Fitness.HISTORY_API)
                    .addApi(Fitness.CONFIG_API)
                     /* .addScope(Scope(Scopes.FITNESS_ACTIVITY_READ))
                      .addScope(Scope(Scopes.FITNESS_ACTIVITY_READ_WRITE))*/
                    .addScope(Scope("https://www.googleapis.com/auth/fitness.activity.read"))
                    .addScope(Scope("https://www.googleapis.com/auth/fitness.activity.write"))
                    .addConnectionCallbacks(
                        object : GoogleApiClient.ConnectionCallbacks {
                            override fun onConnected(bundle: Bundle?) {
                                Log.d("GOOGLE_FIT", "Connected!!!")

                                InsertAndVerifyDataTask().execute()

                            }

                            override fun onConnectionSuspended(i: Int) {
                                Log.d("GOOGLE_FIT", "onConnectionSuspended!!!")
                                // If your connection to the sensor gets lost at some point,
                                // you'll be able to determine the reason and react to it here.

                                if (i == GoogleApiClient.ConnectionCallbacks.CAUSE_NETWORK_LOST) {
                                    Log.d(
                                        "GOOGLE_FIT",
                                        "Connection lost.  Cause: Network Lost."
                                    )
                                } else if (i == GoogleApiClient.ConnectionCallbacks.CAUSE_SERVICE_DISCONNECTED) {
                                    Log.d(
                                        "GOOGLE FIT",
                                        "Connection lost.  Reason: Service Disconnected"
                                    )
                                } else {

                                }
                            }
                        }

                    )
                    .enableAutoManage(activity as FragmentActivity, 0) { result ->
                        Log.d(
                            "GOOGLE FIT",
                            "Google Play services connection failed. Cause: $result"
                        )
                    }
                    .build()
           /* } else {
              //  InsertAndVerifyDataTask().execute()

            }*/

        } catch (e: Exception) {
            Log.d("GOOGLE_FIT_build", e.toString())
        }
    }

    inner class InsertAndVerifyDataTask : AsyncTask<Void?, Void?, Void?>() {

        var stepscount: String? = null
        var calories = "0"

        override fun doInBackground(vararg p0: Void?): Void? {
            //FETCHING TOTAL STEPS COUNT OF THE DAY//
            Log.e("insertandverifydatastep", "doinback")
            var total: Long = 0
            val result = Fitness.HistoryApi.readDailyTotal(mClient!!, DataType.TYPE_STEP_COUNT_DELTA)
            //PendingResult<DailyTotalResult> result = Fitness.HistoryApi.readDailyTotal(googleApiClient, DataType.TYPE_STEP_COUNT_DELTA);
            val totalResult = result.await(1, TimeUnit.MINUTES)
            Log.e("insertandvstep", "doinback7" + totalResult.status)
            if (totalResult.status.isSuccess) {
                Log.e("insertandvestepsucc", "issuccess")
                val totalSet = totalResult.total
                total =
                    if (totalSet!!.isEmpty) 0 else totalSet.dataPoints[0].getValue(Field.FIELD_STEPS)
                        .asInt().toLong()
                Log.e("insertandvestepsucc", "issuccess2")
                /* activiity.runOnUiThread(java.lang.Runnable {
                     Toast.makeText(context, "Success: " + total.toString(), Toast.LENGTH_SHORT)
                         .show()
                 })*/
            } else {
                /* activiity.runOnUiThread(java.lang.Runnable {
                     Toast.makeText(context, "Fail: " + total.toString(), Toast.LENGTH_SHORT).show()
                 })*/

                Log.e("insertandvestepsucc", "issuccess3")
                Log.w("", "There was a problem getting the step count.")
            }
            Log.e("Totalsteps", "Total steps: $total")
            //Toast.makeText(Home.this,"Step count"+total,Toast.LENGTH_LONG).show();
            stepscount = total.toString()

            //END OF FETCHING TOTAL STEPS COUNT OF THE DAY//
            Log.e("Totalsteps2", "Total steps: $stepscount")
            //FETCHING CALORIES DATA//
            val readRequest = FitpassHealthManager.queryFitnessData()
            val dataReadResult =
                Fitness.HistoryApi.readData(mClient!!, readRequest).await(1, TimeUnit.MINUTES)
            //DataReadResult dataReadResult = Fitness.HistoryApi.readData(googleApiClient, readRequest).await(1, TimeUnit.MINUTES);
            printData(dataReadResult)
            Log.e("TotalCalories", "Total calories: $calories")
            activity.runOnUiThread(Thread {
                Toast.makeText(activity,"Totalsteps: "+stepscount.toString()+" Totalcalories: "+calories,Toast.LENGTH_SHORT).show()
            })
            mClient!!.stopAutoManage(context as FragmentActivity);
            mClient!!.disconnect();
            return null
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)


        }

        private fun printData(dataReadResult: DataReadResult) {
            if (dataReadResult.buckets.size > 0) {
                Log.e("", "Number of returned buckets of DataSets is: " + dataReadResult.buckets.size)
                for (bucket in dataReadResult.buckets) {
                    val dataSets = bucket.dataSets
                    for (dataSet in dataSets) {
                        dumpDataSet(dataSet)
                    }
                }
            } else if (dataReadResult.dataSets.size > 0) {
                Log.e(
                    "", "Number of returned DataSets is: "
                            + dataReadResult.dataSets.size
                )
                for (dataSet in dataReadResult.dataSets) {
                    dumpDataSet(dataSet)
                }
            }
        }

        private fun dumpDataSet(dataSet: DataSet) {
            Log.e("", "Data returned for Data type: " + dataSet.dataType.name)
            val dateFormat = DateFormat.getTimeInstance()
            for (dp in dataSet.dataPoints) {
                Log.e("Data point", "Data point:")
                Log.e("type", "\tType: " + dp.dataType.name)
                Log.e(
                    "start",
                    "\tStart: " + dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS))
                )
                Log.e("end", "\tEnd: " + dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)))
                for (field in dp.dataType.fields) {
                    Log.e("field", "\tField: " + field.name + " Value is of: " + dp.getValue(field))
                    val a = dp.getValue(field).asFloat()
                    val b = Math.round(a)
                    calories = b.toString()
                }
            }
        }

    }


}