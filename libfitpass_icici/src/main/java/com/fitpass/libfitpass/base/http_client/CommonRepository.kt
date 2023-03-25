package com.fitpass.libfitpass.home.http_client

import android.app.Activity
import android.content.Context
import android.util.Log
import com.fitpass.libfitpass.R
import com.fitpass.libfitpass.base.constants.ConfigConstants
import com.fitpass.libfitpass.base.customview.CustomToastView
import com.fitpass.libfitpass.base.dataencription.RandomKeyGenrator
import com.fitpass.libfitpass.base.http_client.ApiClient
import com.fitpass.libfitpass.base.http_client.CustomLoader
import com.fitpass.libfitpass.base.http_client.ErrorResponse
import com.fitpass.libfitpass.base.utilities.FitpassNetworkUtil
import com.fitpass.libfitpass.base.utilities.Util
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class CommonRepository(val mContext: Context, val mActivity: Activity) {
    companion object {

        lateinit var activitynew: Activity
        var apiEndUrl = ""
        var apiBaseUrl = ""
        var apiRequest = ""
        var apiIsLoaderVisible = true
        var apiEncryptionType: Int = 1
        var isCallApiWithNormal: Boolean = true
        var IsapiCallWithArrayBody: Boolean = false
        var IsapiCallWithMultipart1: Boolean = false
        var IsapiCallWithMultipart2: Boolean = false
        var IsapiCallWithMultipart3: Boolean = false
        lateinit var handleResponseListeners1: HandleResponseListeners
    }
    lateinit var handleResponseListeners: HandleResponseListeners

    suspend fun getHomeData(
        endPointUrl: String,
        request: String,
        paramCount: Int,
        handleResponseListeners: HandleResponseListeners) {
        this.handleResponseListeners = handleResponseListeners
        Log.d("Data_encrypted",request+"..")
       // Log.d("Data_decripted", RandomKeyGenrator.decrptBODYFile(request).toString()+"..")
        val apiService = ApiClient.getApiClient(mContext, paramCount).create(ApiInterface::class.java)
         var jsonObject = apiService.getHomeDataApi(request)
        Log.e("server response", jsonObject.toString())
        handleResponse(jsonObject, endPointUrl)
    }
    suspend fun getScanData(
        endPointUrl: String,
        request: JSONObject,
        paramCount: Int,
        handleResponseListeners: HandleResponseListeners) {
        this.handleResponseListeners = handleResponseListeners

      // Log.d("Data_encrypted",request+"..")
        // Log.d("Data_decripted", RandomKeyGenrator.decrptBODYFile(request).toString()+"..")
        val apiService = ApiClient.getApiClient3(mContext, paramCount).create(ApiInterface::class.java)
        var jsonObject = apiService.getScanDataApi(Util.converJSONTojson(request.toString()))

        Log.e("server response", jsonObject.toString())
        handleResponse(jsonObject, endPointUrl)
    }

    suspend fun getICICIHomeData(
        endPointUrl: String,
        request: JSONObject,
        paramCount: Int,
        handleResponseListeners: HandleResponseListeners) {
        this.handleResponseListeners = handleResponseListeners
        Log.d("request->",request.toString())
        // Log.d("Data_decripted", RandomKeyGenrator.decrptBODYFile(request).toString()+"..")
        val apiService = ApiClient.getApiClient3(mContext, paramCount).create(ApiInterface::class.java)
        var requestHashMap=Util.converJSONTojson(request.toString())
        var jsonObject = apiService.getIciciHomeDataApi(ApiConstants.ICICI_HOME_API,requestHashMap!!)
        Log.e("serverresponse", jsonObject.toString())
        handleResponse(jsonObject, endPointUrl)
    }
    fun apiCallNew(
        endPointUrl: String,
        request: JSONObject?,
        handleResponseListenersInput: HandleResponseListeners,
        isLoaderVisible: Boolean,
        baseurl: String,
        encryptionType: Int
    ) {
        apiEndUrl = endPointUrl
        apiBaseUrl = baseurl
        apiIsLoaderVisible = isLoaderVisible
        handleResponseListeners1 = handleResponseListenersInput
        apiEncryptionType = encryptionType!!
        isCallApiWithNormal = true
        IsapiCallWithArrayBody = false
        IsapiCallWithMultipart1 = false
        IsapiCallWithMultipart2 = false
        IsapiCallWithMultipart3 = false
        var encryptedData = ""
        var jsonObject: retrofit2.Response<com.google.gson.JsonObject?>? = null
        this.handleResponseListeners = handleResponseListenersInput
        Log.d("requestvalue", request.toString())
        val encryptKey = RandomKeyGenrator.getAlphaNumericString(16)
        RandomKeyGenrator.setRandomKey(encryptKey)
        if (request != null) {
            apiRequest = request.toString()
        } else {
            apiRequest = ""
        }

        if (FitpassNetworkUtil.checkInternetConnection(mContext)) {
            if (isLoaderVisible) {
                CustomLoader.showLoaderDialog(mActivity, mContext)
            }
            //   if (Utils.getIntertNetSpeed(mContext) > 14) {
            if (request != null) { // for post

                var paramSize = Util.getmapSize(request.toString())
                if (encryptionType == ConfigConstants.ENCRYPTION_DYNAMIC_IV) {
                    encryptedData = Util.encrptdataWithKey(request.toString(), encryptKey)!!
                } else if (encryptionType == ConfigConstants.ENCRYPTION_STATIC_IV) {
                    encryptedData = Util.encrptBodyDataForGoWithKey(request.toString(), encryptKey)!!
                }
                Log.d("encryptedRequest", encryptedData.toString())
                val apiService = ApiClient.getApiClientNew(
                    mContext,
                    paramSize,
                    ConfigConstants.HEADER_TYPE_APPLICATION_JSON,
                    encryptKey,
                    encryptionType, baseurl
                ).create(ApiInterface::class.java)
                if (!encryptedData.isNullOrEmpty()) {
                    val call = apiService.commonPostMethodServerRequest(endPointUrl, encryptedData)
                    requestPostCall(endPointUrl, call)

                } else {
                    Log.d("jsonrequestrequest", request.toString())
                    var requestnew=Util.converJSONTojson(request.toString())
                    val call: Call<JsonObject?> =
                        apiService.commonPostMethodServerRequest(endPointUrl, requestnew!!)
                    requestPostCall(endPointUrl, call)
                }

            } else { //For Get

                val apiService = ApiClient.getApiClientNew(
                    mContext,
                    0,
                    ConfigConstants.HEADER_TYPE_APPLICATION_JSON,
                    encryptKey,
                    encryptionType, baseurl
                ).create(ApiInterface::class.java)
                val call: Call<JsonObject?> =
                    apiService.commonGetMethodServerRequest(endPointUrl)
                requestPostCall(endPointUrl, call)

            }
            /*  } else {
                  var intent = Intent(mContext, NoInternetActivity::class.java)
                  mContext.startActivity(intent)
              }*/
        } else {
          /*  var intent = Intent(mContext, NoInternetActivity::class.java)
            mContext.startActivity(intent)*/

            CoroutineScope(Dispatchers.Main).launch {
                CustomToastView.errorToasMessage(
                    mActivity,
                    mContext,
                    mContext.resources.getString(R.string.no_internet)
                )
            }
        }
        Log.d("apiRequest", apiRequest + " ....")
    }



    fun handleResponse(data: Response<JsonObject?>?, url: String) {
        if (data != null && data!!.isSuccessful) {
            Log.e("response Received", data?.body().toString())
            val response = JSONObject(data!!.body().toString())
            if (response != null) {
                if (response!!.has("code")) {
                    var responseCode = response!!.optInt("code")
                    if (responseCode == 401 ||  responseCode == 502) {
                        handleResponseListeners.handleErrorMessage(response?.optString("message"), url)
                    } else if (responseCode == 200) {
                        handleResponseListeners.handleSuccess(data, url)

                    } else if (responseCode == 412) {
                        handleResponseListeners.handleErrorMessage(response?.optString("message"), url)

                    } else {

                        CustomToastView.errorToasMessage(mActivity, mContext, "" + response.getString("message"))
                        handleResponseListeners.handleErrorMessage(
                            response?.optString("message"),
                            url
                        )
                    }
                }
            }

        } else {
/*{"code":400,"message":"Invalid request parameters. Please check settings and try again."}
*/
            CustomLoader.hideLoaderDialog(mActivity)
            if (data != null) {
                if (data!!.code() == 401 || data!!.code() > 500) {
                    //FitpassAplicationActivity.getInstance().userLogout()
                } else if (data?.code() == 412) {
                    val errorBody: ResponseBody = data!!.errorBody()!!
                    val error: ErrorResponse =
                        Gson().fromJson(errorBody.charStream(), ErrorResponse::class.java)
                    var JsonObject = Gson().toJson(error)
                    var erroJsonObject = JSONObject(JsonObject)

                    CustomToastView.errorToasMessage(
                        mActivity,
                        mContext,
                        "" + erroJsonObject.getString("message")
                    )


                } else {
                    val errorBody: ResponseBody = data!!.errorBody()!!
                    val error: ErrorResponse =
                        Gson().fromJson(errorBody.charStream(), ErrorResponse::class.java)
                    var JsonObject = Gson().toJson(error)
                    var erroJsonObject = JSONObject(JsonObject)
                    // handleResponseListeners.handleErrorMessage(erroJsonObject?.optString("message"),url)

                    CustomToastView.errorToasMessage(
                        mActivity,
                        mContext,
                        "" + erroJsonObject.getString("message")
                    )


                }
            }

        }

    }

    fun requestPostCall(endPointUrl: String, call: Call<JsonObject?>) {
        try {
            call.enqueue(object : Callback<JsonObject?> {
                override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>) {

                    if (response.isSuccessful()) {
                        if (response.code() == 200) {
                            val jsonObject: JSONObject = JSONObject(response.body().toString())
                            Log.d("apicallcesponse", jsonObject.toString())
                            //listenner.handleResponse(jsonObject.toString(),url)
                            handleResponse(response, endPointUrl)
                        } else {
                            CustomLoader.hideLoaderDialog(mActivity)
                            CoroutineScope(Dispatchers.Main).launch {
                                CustomToastView.errorToasMessage(
                                    mActivity,
                                    mContext,
                                    "Something went wrong"
                                )
                            }
                            // listenner.handleErrorMessage(mcontext.resources.getString(R.string.errormsg),url)
                        }
                    } else {
                        //  listenner.handleErrorMessage(mcontext.resources.getString(R.string.errormsg),url)
                        if(response.code()>=500){
                            Log.d("requestPostCall","requestPostCall2")
                        }
                        CustomLoader.hideLoaderDialog(mActivity)
                        CoroutineScope(Dispatchers.Main).launch {
                            CustomToastView.errorToasMessage(
                                mActivity,
                                mContext,
                                "Something went wrong"
                            )
                        }
                    }
                }

                override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                    //listenner.handleResponse(t.toString(),url)
                    Log.d("onFailure", t.toString())
                    CustomLoader.hideLoaderDialog(mActivity)
                    CoroutineScope(Dispatchers.Main).launch {
                        CustomToastView.errorToasMessage(
                            mActivity,
                            mContext,
                            "Something went wrong"
                        )
                    }
                  /*  var intent = Intent(mContext, NoInternetActivity::class.java)
                    mContext.startActivity(intent)*/
                    /*  CoroutineScope(Dispatchers.Main).launch {
                          CustomErrorToasView.errorToasMessage(
                              mActivity,
                              mContext,
                              t.toString()
                          )
                      }*/

                }
            })
        } catch (e: java.lang.Exception) {


        }
    }
}