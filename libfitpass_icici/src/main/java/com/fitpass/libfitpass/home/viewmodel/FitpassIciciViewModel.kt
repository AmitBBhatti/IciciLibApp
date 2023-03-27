package com.fitpass.libfitpass.home.viewmodel

import android.app.Activity
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitpass.libfitpass.base.customview.CustomToastView
import com.fitpass.libfitpass.base.http_client.CustomLoader
import com.fitpass.libfitpass.base.utilities.FitpassPrefrenceUtil
import com.fitpass.libfitpass.base.utilities.Util
import com.fitpass.libfitpass.home.http_client.ApiConstants
import com.fitpass.libfitpass.home.http_client.CommonRepository
import com.fitpass.libfitpass.home.http_client.HandleResponseListeners
import com.fitpass.libfitpass.home.icicimenumodel.Product_List
import com.fitpass.libfitpass.home.icicimenumodel.ViewAllWorkout
import com.fitpass.libfitpass.home.icicimenumodel.Workoutlist
import com.fitpass.libfitpass.home.listeners.FitpassiciciHomeListener
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Response
import java.lang.reflect.Type
import com.fitpass.libfitpass.home.icicimenumodel.List


class FitpassIciciViewModel(
    val commonRepository: CommonRepository,
    val context: Context,
    val activity: Activity,val fitpassiciciHomeListener: FitpassiciciHomeListener
) : ViewModel(), HandleResponseListeners {
    var productList = MutableLiveData<ArrayList<Product_List>>()
    var workoutList = MutableLiveData<ArrayList<Workoutlist>>()
    var isApiHit = MutableLiveData<Boolean>()
    var hasCopyRight = MutableLiveData<Boolean>()
    var slide_webview = MutableLiveData<String>()
    var viewallworkout = MutableLiveData<String>()
    var faqheading=MutableLiveData<String>()
    var copyright=MutableLiveData<String>()
    var whiteListUrl=MutableLiveData<String>()
    var faqList=MutableLiveData<ArrayList<List>>()
    var isShowScanButton=MutableLiveData<Boolean>()
    private var handleResponseListeners: HandleResponseListeners? = null

    init {
        isShowScanButton.value=false
        faqheading.value=""
        viewallworkout.value=""
        slide_webview.value=""
        isApiHit.value=false
        handleResponseListeners = this@FitpassIciciViewModel
        copyright.value=""
        whiteListUrl.value=""
        hasCopyRight.value=false
    }
    fun getHomeData(requestBody: JSONObject) {
        viewModelScope.launch(Dispatchers.Main)
        {

          //  val dynamicSecretKey = RandomKeyGenrator.generate16DigitRandom()
          //  RandomKeyGenrator.setRandomKey(dynamicSecretKey)
           // val encryptedData = RandomKeyGenrator.encrptBodydataWithRandomKey(requestBody.toString())
           /* withContext(Dispatchers.Main)
            {
                CustomLoader.showLoaderDialog(activity, context)
            }*/
            async(Dispatchers.IO) {

              commonRepository.getICICIHomeData(
                    ApiConstants.ICICI_HOME_API,
                    requestBody,
                    Util.getmapSize(requestBody.toString()),
                    handleResponseListeners!!
                )

                //commonRepository!!.apiCallNew(ApiConstants.ICICI_HOME_API, requestBody, handleResponseListeners!!, true,ApiConstants.BASET_URL, ConfigConstants.NO_ENCRYPTION);

            }.await()

        }

    }

    override fun handleSuccess(response1: Response<JsonObject?>, api: String?) {
        var gson = Gson()
        var jsonResponse=JSONObject(response1.body().toString())
        var code=jsonResponse.getString("code")
        var message="Some thing went wrong"
       // isApiHit.value=true
        if(code.equals("200")){

            if(jsonResponse.has("results")){

                var jsonResult= jsonResponse.getJSONObject("results")
                if(jsonResult.has("copyright_string"))
                {
                    activity.runOnUiThread(Runnable {
                            copyright.value = jsonResult.optString("copyright_string");
                        if(!copyright!!.value!!.isNullOrEmpty()){
                            hasCopyRight.value=true
                        }
                        })
                }

                if(jsonResult.has("white_list_url"))
                {
                    activity.runOnUiThread(Runnable {

                        whiteListUrl.value = jsonResult.optString("white_list_url");
                        FitpassPrefrenceUtil.setStringPrefs(context, FitpassPrefrenceUtil.WHITE_LIST_URL,jsonResult.optString("white_list_url"))
                    })
                }

                if(jsonResult.has("slide_webview"))
                {
                    if(jsonResult.optString("slide_webview")!=null){
                        activity.runOnUiThread(Runnable {
                            slide_webview.value = jsonResult.optString("slide_webview");
                        })
                    }
                }
                if(jsonResult.has("view_all_workout"))
                {
                    var jsonObject=jsonResult.get("view_all_workout")
                    if(jsonObject is JSONObject) {
                        activity.runOnUiThread(Runnable {
                            viewallworkout.value = jsonObject.toString()


                        })
                    }

                }
                if(jsonResult.has("product_list")){
                    var jsonArray=jsonResult.get("product_list")
                    if(jsonArray is JSONArray){
                        activity.runOnUiThread(Runnable {
                            val listType: Type = object : TypeToken<ArrayList<Product_List?>?>() {}.type
                            productList.value = gson.fromJson(jsonArray.toString(), listType)
                        })
                    }
                }
                if(jsonResult.has("workoutlist")){
                    var jsonArray=jsonResult.get("workoutlist")
                    if(jsonArray is JSONArray){
                        activity.runOnUiThread(Runnable {
                            val listType: Type = object : TypeToken<ArrayList<Workoutlist?>?>() {}.type
                            workoutList.value = gson.fromJson(jsonArray.toString(), listType)

                            if(workoutList.value!!.size>0){
                                if(workoutList!!.value!!.get(0).workout_status.equals("1")){
                                    isShowScanButton.value=true

                                }

                            }
                        })
                    }
                }
                if(jsonResult.has("faq")){
                    var jsonObject=jsonResult.get("faq")
                    if(jsonObject is JSONObject){
                        var jsonArray=jsonObject.get("list")
                        if(jsonArray is JSONArray){
                            activity.runOnUiThread(Runnable {
                                faqheading.value=jsonObject.optString("heading")
                                val listType: Type = object : TypeToken<ArrayList<com.fitpass.libfitpass.home.icicimenumodel.List?>?>() {}.type
                                faqList.value = gson.fromJson(jsonArray.toString(), listType)
                            })
                        }

                    }
                }
                if(jsonResult.has("user_details")){
                    var jsonObject=jsonResult.optJSONObject("user_details")
                    FitpassPrefrenceUtil.setStringPrefs(context, FitpassPrefrenceUtil.APP_KEY, jsonObject.optString("app_key"))
                    FitpassPrefrenceUtil.setStringPrefs(context, FitpassPrefrenceUtil.USER_ID,jsonObject.optString("user_id"))
                    FitpassPrefrenceUtil.setStringPrefs(context, FitpassPrefrenceUtil.SECRET_KEY, jsonObject.optString("secret_key"))
                    FitpassPrefrenceUtil.setStringPrefs(context, FitpassPrefrenceUtil.AUTH_TOKEN, jsonObject.optString("auth_token"))
                }

            }
            //CustomLoader.hideLoaderDialog(activity)
        }else{
            CustomToastView.errorToasMessage(activity, context, message)
            //CustomLoader.hideLoaderDialog(activity)
        }

    }

    override fun handleErrorMessage(response: String?, api: String?) {
        //isApiHit.value=true
        CustomToastView.errorToasMessage(activity, context, response)
       // CustomLoader.hideLoaderDialog(activity)
    }

}