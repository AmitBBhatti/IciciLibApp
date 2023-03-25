package com.fitpass.libfitpass.fitpasschallenge

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.fitpass.libfitpass.R
import com.fitpass.libfitpass.base.constants.ConfigConstants
import com.fitpass.libfitpass.databinding.ActivityChallenge2Binding

class FitpassChallengeActivity : AppCompatActivity() {
    private var challengeBinding: ActivityChallenge2Binding? = null

    fun finishTask() {
        finish()
    }

    fun hideBars(binding: ActivityChallenge2Binding) {

        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        val window = window
        window.statusBarColor = resources.getColor(R.color.white)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        challengeBinding = DataBindingUtil.setContentView(this, R.layout.activity_challenge2)
        hideBars(challengeBinding!!)
        var bundle = Bundle()
        bundle.putString(ConfigConstants.NAVIGATION_SOURCE, "");

        var challengeFragment: Fragment = FitpassChallengeFragment();
        challengeFragment.arguments = bundle
        supportFragmentManager?.beginTransaction()?.replace(R.id.fr_base, challengeFragment)
            ?.commitAllowingStateLoss()
    }
}