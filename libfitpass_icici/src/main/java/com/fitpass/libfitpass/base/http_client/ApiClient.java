package com.fitpass.libfitpass.base.http_client;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.fitpass.libfitpass.base.constants.ConfigConstants;
import com.fitpass.libfitpass.base.dataencription.RandomKeyGenrator;
import com.fitpass.libfitpass.base.utilities.FitpassDeviceInfoUtil;
import com.fitpass.libfitpass.base.utilities.FitpassPrefrenceUtil;
import com.fitpass.libfitpass.base.utilities.Util;
import com.fitpass.libfitpass.home.http_client.ApiConstants;

import org.json.JSONObject;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiClient {

    static Context context;
    //private static DataStore ds = DataStore.getInstance();
    static int parameterCount = 0;
    static int contentypeGlobal=0;

   /* public static  String getBaseUrl() {
        if (Const.APP_ENVIRONMENT == 1)
            return "https://api-fitpass.in/";
            //   return "https://api.fitpass.dev/";
        else
            return "https://api.fitpass.dev/";
    }*/

    public static Retrofit getApiClient(Context mContext, int paraCount) {

        context = mContext;
        parameterCount = paraCount;
        Retrofit retrofit = null;

        if (retrofit == null) {
            CookieManager cookieManager = new CookieManager();
            cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
            HashMap<String, String> headers = new HashMap<>();
            //  CustomRequestInterceptor customRequestInterceptor=new CustomRequestInterceptor(headers);

            OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder().addInterceptor(new HeaderIntercepter());
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            clientBuilder.addInterceptor(new NetworkConnectionInterceptor(mContext));
            clientBuilder.addInterceptor(loggingInterceptor);
            retrofit = new Retrofit.Builder()
                    .baseUrl(ApiConstants.BASET_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(clientBuilder.build())
                    .build();

        }
        return retrofit;
    }
    public static Retrofit getApiClient3(Context mContext, int paraCount) {

        context = mContext;
        parameterCount = paraCount;
        Retrofit retrofit = null;

        if (retrofit == null) {
            CookieManager cookieManager = new CookieManager();
            cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
            HashMap<String, String> headers = new HashMap<>();
            //  CustomRequestInterceptor customRequestInterceptor=new CustomRequestInterceptor(headers);

            OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder().addInterceptor(new HeaderIntercepter1());
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            clientBuilder.addInterceptor(new NetworkConnectionInterceptor(mContext));
            clientBuilder.addInterceptor(loggingInterceptor);
            retrofit = new Retrofit.Builder()
                    .baseUrl(ApiConstants.BASET_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(clientBuilder.build())
                    .build();

        }
        return retrofit;
    }

    public static Retrofit getApiClientNew(Context mContext,int paraCount,int contentType,String randomkey,int encryptionType,String baseurl) {
        context = mContext;
        parameterCount = paraCount;
        contentypeGlobal=contentType;
//        Retrofit retrofit = null;
        // if (retrofit == null) {
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        HashMap<String, String> headers = new HashMap<>();
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder().connectTimeout(2, TimeUnit.MINUTES)
                .writeTimeout(2, TimeUnit.MINUTES)
                .readTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(new HeaderIntercepter());
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        //clientBuilder.addInterceptor(new NetworkConnectionInterceptor(mContext));
        clientBuilder.addInterceptor(loggingInterceptor);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseurl).addConverterFactory(GsonConverterFactory.create()).client(clientBuilder.build()).build();

        // }
        return retrofit;
    }
    public static class HeaderIntercepter implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            JSONObject payloadObject = new JSONObject();
            String dynamicSecretKey = RandomKeyGenrator.getRandomKey();
            try {
                if (FitpassPrefrenceUtil.INSTANCE.getStringPrefs(context, FitpassPrefrenceUtil.INSTANCE.getAPP_KEY(), "").isEmpty()) {
                    payloadObject.put("app_key", ConfigConstants.Companion.getAPP_KEY());
                } else {
                    payloadObject.put("app_key", FitpassPrefrenceUtil.INSTANCE.getStringPrefs(context, FitpassPrefrenceUtil.INSTANCE.getAPP_KEY(), ""));
                }
                payloadObject.put("auth_key", ConfigConstants.Companion.getAUTH_KEY());
                payloadObject.put("device_name", FitpassDeviceInfoUtil.getMobileName(context));
                payloadObject.put("device_os", FitpassDeviceInfoUtil.getMobileOs(context));
                payloadObject.put("sdk_version", ConfigConstants.Companion.getSDK_VERSION());
                payloadObject.put("dynamic_secrety_key", dynamicSecretKey);
                payloadObject.put("user_id", FitpassPrefrenceUtil.INSTANCE.getStringPrefs(context, FitpassPrefrenceUtil.INSTANCE.getUSER_ID(), ""));
            } catch (Exception e) {

            }
            Log.d("payloadObject", payloadObject.toString());
            String encripteddata=RandomKeyGenrator.encrptBodydata(payloadObject.toString());
            Log.d("payloadObjectdecrp",  encripteddata);
            Request tokenRequest = request.newBuilder()
                    .addHeader("Content-Type", "application/json")
                    .addHeader("X-SDK-VERSION", ConfigConstants.Companion.getSDK_VERSION())
                    .addHeader("X-DEVICE-TYPE", ConfigConstants.Companion.getX_DEVICE_TYPE())
                    .addHeader("X-FITPASS-PAYLOAD", encripteddata).build();
            return chain
                    .proceed(tokenRequest);
        }
    }

    public static class HeaderIntercepter1 implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Request tokenRequest = request.newBuilder()
                    .addHeader("Content-Type", "application/json")
                    //.addHeader("X-DEVICE-TYPE", ConfigConstants.Companion.getX_DEVICE_TYPE())
                    .addHeader("X-DEVICE-TYPE", "web")
                    .build();
            return chain
                    .proceed(tokenRequest);
        }
    }


/*
    public static class HeaderIntercepterNew implements Interceptor {
        String randomkey="";
        int encryptionType;
        HeaderIntercepterNew(String randomkey, int encryption){
            this.randomkey=randomkey;
            this.encryptionType=encryption;
        }
        @Override
        public Response intercept(Chain chain) throws IOException {


            JSONObject json=new JSONObject();
            try {
                String appKey="";
                String deviceToken="";
                if (!ds.getAPP_KEY(context).equalsIgnoreCase("")) {
                    appKey= ds.getAPP_KEY(context);
                }
                else {
                    appKey= Utils.STATIC_APP_KEY;
                }
                if (!ds.getDeviceToken(context).equalsIgnoreCase("")) {
                    deviceToken= ds.getDeviceToken(context);
                }
                else {
                    deviceToken= "test-device-toke";
                }
                if (DataStore.getInstance().getUSER_ID(FitpassAplicationActivity.getInstance().getApplicationContext())!=null
                        &&!DataStore.getInstance().getUSER_ID(FitpassAplicationActivity.getInstance().getApplicationContext()).equalsIgnoreCase("")) {
                    json.put("user_id", Integer.parseInt(DataStore.getInstance().getUSER_ID(FitpassAplicationActivity.getInstance().getApplicationContext())));
                }
                json.put("request_parameter_count", parameterCount);
                json.put("app_version", BuildConfig.VERSION_NAME);
                json.put("device_type", Const.DEVICE_TYPE);
                json.put("device_token", deviceToken);
                json.put("device_name", Build.MODEL);
                json.put("device_os", Utils.getDeviceOS());
                json.put("app_key", appKey);
                json.put("device_id", Utils.getInstance().getDeviceId(context));
                json.put("dynamic_key", randomkey);
                Log.d("payloadbody",json.toString());
            }
            catch (Exception e){

            }
            Request request = chain.request();
            //  Request.Builder requestBuilder = original.newBuilder();
            Request.Builder requestBuilder = request.newBuilder();
            String encryptedPayload="";

            if (encryptionType== ProjectConstants.ENCRYPTION_STATIC_IV)
            {
                Log.d("isPhpUrl","false");
                encryptedPayload= new Utils().encrptdataForGo(json.toString());
            }
            else if (encryptionType== ProjectConstants.ENCRYPTION_DYNAMIC_IV)
            {
                Log.e("before encryption",json.toString());
                encryptedPayload=new Utils().newMethodToEncrptdata(json.toString());
                Log.e("after  encryption",encryptedPayload);

            }
            else {
                encryptedPayload=new Utils().encrptdata(json.toString());

            }

            if (contentypeGlobal==Constants.HEADER_TYPE_APPLICATION_JSON)
            {
                Request tokenRequest = request.newBuilder()
                        .addHeader("Content-Type", Constants.CONTNENT_TYPE_APPLICATION_TEXT)
                        .addHeader("X-FITPASS-PAYLOAD", encryptedPayload)
                        .addHeader("X-DEVICE-TYPE", Const.DEVICE_TYPE)
                        .addHeader("X-APP-VERSION", BuildConfig.VERSION_NAME)
                        .addHeader("X-DEVICE-NAME", Build.MODEL)
                        .addHeader("X-DEVICE-OS", Build.VERSION.RELEASE)
                        .addHeader("X-APPKEY", ds.getAPP_KEY(context))
                        .addHeader("X-AUTHKEY", Utils.STATIC_AUTH_KEY)
                        .addHeader("X-DEVICE-TOKEN", ds.getDeviceToken(context))
                        .addHeader("X-DEVICE-ID", Utils.getInstance().getDeviceId(context))
                        .build();
                return chain.proceed(tokenRequest);

            }
            else{
                Request tokenRequest = request.newBuilder()
                        .addHeader("Content-Type", Constants.CONTNENT_TYPE_MULTIPART)
                        .addHeader("X-FITPASS-PAYLOAD", encryptedPayload)
                        .addHeader("X-DEVICE-TYPE", Const.DEVICE_TYPE)
                        .addHeader("X-APP-VERSION", BuildConfig.VERSION_NAME)
                        .addHeader("X-DEVICE-NAME", Build.MODEL)
                        .addHeader("X-DEVICE-OS", Build.VERSION.RELEASE)
                        .addHeader("X-APPKEY", ds.getAPP_KEY(context))
                        .addHeader("X-AUTHKEY", Utils.STATIC_AUTH_KEY)
                        .addHeader("X-DEVICE-TOKEN", ds.getDeviceToken(context))
                        .addHeader("X-DEVICE-ID", Utils.getInstance().getDeviceId(context))
                        .build();
                return chain
                        .proceed(tokenRequest);

            }

        }
    }
*/


}



