package com.fitpass.libfitpass.home.http_client

import com.google.gson.JsonObject
import okhttp3.MultipartBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiInterface {
    @POST(ApiConstants.HOME_API)
    suspend fun getHomeDataApi(@Body request: String?): Response<JsonObject?>?
    @POST(ApiConstants.SCANQRCODE_API)
    suspend fun getScanDataApi(@Body request: JsonObject?): Response<JsonObject?>?


    @POST
    fun commonPostMethodServerRequest(@Url endpoint:String, @Body requestBody: String): Call<JsonObject?>
    @POST
    fun commonPostMethodServerRequest(@Url endpoint:String, @Body requestBody: JsonObject): Call<JsonObject?>

    @GET
    fun commonGetMethodServerRequest(@Url endpoint:String): Call<JsonObject?>

    @POST
    suspend fun getIciciHomeDataApi(@Url endpoint:String,@Body request: JsonObject): Response<JsonObject?>?

    @Multipart
    @POST
    fun AddNote(@Url endpoint:String, @Part user_photo: MultipartBody.Part?, @Part("request_body") request_body: String?): Call<JsonObject?>

    @Multipart
    @POST
    fun updateFeedback(@Url endpoint:String, @Part user_photo: MultipartBody.Part?, @Part("body_payload") body_payload: String?): Call<JsonObject?>

}