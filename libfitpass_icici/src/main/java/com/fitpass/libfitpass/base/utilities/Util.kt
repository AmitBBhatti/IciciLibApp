package com.fitpass.libfitpass.base.utilities

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.util.Log
import android.widget.TextView
import com.fitpass.libfitpass.base.constants.ConfigConstants
import com.fitpass.libfitpass.base.dataencription.CryptLib
import com.fitpass.libfitpass.fontcomponent.FontAwesome
import com.fitpass.libfitpass.scanqrcode.activitymodels.ActivityConfig
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import org.json.JSONObject
import java.security.NoSuchAlgorithmException
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.crypto.NoSuchPaddingException


object Util {
    fun setimage(textView: FontAwesome, image: Int) {
        textView?.text = image.toChar().toString()
    }

    fun drawCircle(backgroundColor: String): GradientDrawable? {
        val shape = GradientDrawable()
        shape.shape = GradientDrawable.OVAL
        shape.cornerRadii = floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f)
        shape.setColor(Color.parseColor(backgroundColor))
        return shape
    }

    fun drawRectRadious(backgroundColor: String): GradientDrawable? {
        val shape = GradientDrawable()
        shape.shape = GradientDrawable.RECTANGLE
        shape.cornerRadii = floatArrayOf(10f, 10f, 10f, 10f, 10f, 10f, 10f, 10f)
        shape.setColor(Color.parseColor(backgroundColor))
        return shape
    }
    fun drawGradient(startColor:String,endColor:String): GradientDrawable? {
        var gd = GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, intArrayOf(Color.parseColor(startColor), Color.parseColor(endColor)))
        gd.shape = GradientDrawable.RECTANGLE
        gd.cornerRadii = floatArrayOf(10f, 10f, 10f, 10f, 10f, 10f, 10f, 10f)
        return gd
    }

    fun convertMiliesToDD_MM_HH_MMDateTime2(milies: String, isMiliConversionRequired: Boolean): String? {
        var formatter: DateFormat? = null
        formatter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            SimpleDateFormat("dd-MMM-yyyy hh:mm:ss")
        } else {
            SimpleDateFormat("dd-MMM-yyyy  hh:mm:ss")
        }
        val milliSeconds = milies.toLong()
        println(milliSeconds)
        val calendar = Calendar.getInstance()
        if (isMiliConversionRequired) {
            calendar.timeInMillis = milliSeconds * 1000
        } else {
            calendar.timeInMillis = milliSeconds
        }
        val endDate = formatter.format(calendar.time)
        println(endDate)
        val amToAm = endDate.replace("am", "AM")
        return amToAm.replace("pm", "PM")
    }
    fun convertMiliesToDate(milies: String, isMiliConversionRequired: Boolean,outputfarmat:String): String? {
        var formatter: DateFormat? = null
        formatter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            SimpleDateFormat(outputfarmat)
        } else {
            SimpleDateFormat(outputfarmat)
        }
        val milliSeconds = milies.toLong()
        println(milliSeconds)
        val calendar = Calendar.getInstance()
        if (isMiliConversionRequired) {
            calendar.timeInMillis = milliSeconds * 1000
        } else {
            calendar.timeInMillis = milliSeconds
        }
        val endDate = formatter.format(calendar.time)
        println(endDate)
        val amToAm = endDate.replace("am", "AM")
        return amToAm.replace("pm", "PM")
    }
    fun captalizeString(str: String):String{
        val output = str.substring(0, 1).uppercase(Locale.getDefault()) + str.substring(1)
       return output
    }
    fun concatString(str: String,cancatvalue:String):String{
        return cancatvalue+str
    }
    fun setFantIcon(textView: TextView, image: Int) {
        textView.text = image.toChar().toString()
    }

    fun encrptdata(data: String?): String? {
        Log.e("encrptdata Metod Log", data!!)
        var encrptData = ""
        try {
            val cryptLib = CryptLib()
            encrptData = cryptLib.encryptPlainTextWithRandomIV(
                data,
                ConfigConstants.OLD_PAYLOAD_ENCRYPTION_KEY
            )
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            encrptData = e.message.toString()
        } catch (e: NoSuchPaddingException) {
            e.printStackTrace()
            encrptData = e.message.toString()
        } catch (e: Exception) {
            encrptData = e.localizedMessage
        }
        return encrptData
    }
    fun encrptDataWithSecretekey(context: Context,data: String?): String? {
        var encrptData = ""
        try {
            val cryptLib = CryptLib()
            encrptData = cryptLib.encryptPlainTextWithRandomIV(
                data,
                FitpassPrefrenceUtil.getStringPrefs(context,FitpassPrefrenceUtil.SECRET_KEY,"")
            )
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            encrptData = e.message.toString()
        } catch (e: NoSuchPaddingException) {
            e.printStackTrace()
            encrptData = e.message.toString()
        } catch (e: Exception) {
            encrptData = e.localizedMessage
        }
        return encrptData
    }

    fun decrptWithSecretekey(data: String?): JSONObject? {
        return try {
            val cryptLib = CryptLib()
            val decrptedData = cryptLib.decryptCipherTextWithRandomIV(
                data,
                FitpassPrefrenceUtil.SECRET_KEY
            )
            Log.e("decrptff", decrptedData)
            JSONObject(decrptedData)
        } catch (e: java.lang.Exception) {
            Log.d("Exception",e.toString())
            e.printStackTrace()
            null
        }
    }
     fun getWorkoutImage(imageId: Int): String {
         return when (imageId) {
             1 -> "e92f"

             2 -> "e92e"

             3 -> "e92a"

             4 -> "e96d"

             5 -> "e930"

             6 -> "e92d"

             7 -> "e927"

             8 -> "e931"

             9 -> "e937"

             10 -> "e933"

             11 -> "e936"

             12 -> "e970"

             13 -> "e938"

             14 -> "e934"

             15 -> "e932"
             else -> "e927"
         }
     }

    fun isHexColor(colorCode:String):Boolean{
        try {
            val color: Int = Color.parseColor(colorCode)
            return true
        } catch (iae: IllegalArgumentException) {
            // This color string is not valid
            return false
        }
    }

    fun getActivityConfigList(context: Context): ArrayList<ActivityConfig>? {
        var activityConfigList = ArrayList<ActivityConfig>()
        var gson = Gson()
        val activityConfig: String = FitpassPrefrenceUtil.getStringPrefs(context,FitpassPrefrenceUtil.ACTIVITY_DATA,"").toString()
        if(!activityConfig.isNullOrEmpty()){
            val type = object : TypeToken<List<ActivityConfig?>?>() {}.type
            activityConfigList = gson.fromJson<ArrayList<ActivityConfig>>(activityConfig, type)
        }
        return activityConfigList
    }

    fun getmapSize(baseReq: String?): Int {
        return if (baseReq == null || baseReq.isEmpty()) {
            0
        } else {
            val gson = Gson()
            val map = gson.fromJson<Map<*, *>>(
                baseReq,
                MutableMap::class.java
            )
            map.size
        }
    }

    fun encrptdataWithKey(data: String?, key: String?): String? {
        Log.e("encrptdata Metod Log", data!!)
        var encrptData = ""
        try {
            val cryptLib = CryptLib()
            encrptData = cryptLib.encryptPlainTextWithRandomIV(data, key)
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            encrptData = e.message.toString()
        } catch (e: NoSuchPaddingException) {
            e.printStackTrace()
            encrptData = e.message.toString()
        } catch (e: Exception) {
            encrptData = e.localizedMessage
        }
        return encrptData
    }
    fun encrptBodyDataForGoWithKey(data: String?, randomKey: String?): String? {
        Log.e("encrptdata Metod Log", data!!)
        var encrptData = ""
        try {
            val cryptLib = CryptLib()
            encrptData =
                cryptLib.encryptPlainText(data, randomKey, ConfigConstants.IV_KEY)
            Log.e("encrpt data value", encrptData)
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            encrptData = e.message.toString()
        } catch (e: NoSuchPaddingException) {
            e.printStackTrace()
            encrptData = e.message.toString()
        } catch (e: Exception) {
            encrptData = e.localizedMessage
        }
        return encrptData
    }

    fun convertHashMapToJSON(params: HashMap<String?, String?>): JSONObject? {
        return JSONObject(params!!.toMap())
    }

    fun converJSONTojson(request: String?): JsonObject? {
        return JsonParser().parse(request).asJsonObject
    }

    fun setimage(textView: TextView, image: Int) {
        textView.text = image.toChar().toString()
    }


}