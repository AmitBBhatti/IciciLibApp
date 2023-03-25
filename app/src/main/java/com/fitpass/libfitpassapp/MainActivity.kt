package com.fitpass.libfitpassapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

import com.fitpass.libfitpass.base.utilities.FitpassConfig

import com.fitpass.libfitpass.home.FitpassDashboard


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var fitpassConfig= FitpassConfig.getInstance()
        fitpassConfig!!.setHeaderColor("#00305a")
        fitpassConfig!!.setHeaderFontColor("#e77817")
        fitpassConfig!!.setHeaderTitle("FITPASS")
        fitpassConfig!!.setPadding(16)
        var intent=Intent(this,FitpassDashboard::class.java)
        //var intent=Intent(this,Test::class.java)
        intent.putExtra("vendor_id", "210")
        intent.putExtra("policy_number", "106830313")
        intent.putExtra("member_id", "516899111")
        startActivity(intent)
    }
    fun click(view: View) {
        var intent=Intent(this,FitpassDashboard::class.java)
        //var intent=Intent(this,Test::class.java)
        intent.putExtra("vendor_id", "210")
        intent.putExtra("policy_number", "106830313")
        intent.putExtra("member_id", "516899111")
        /* intent.putExtra("policy_number", "10683033")
        intent.putExtra("member_id", "51689915")*/
        startActivity(intent)
    }
}