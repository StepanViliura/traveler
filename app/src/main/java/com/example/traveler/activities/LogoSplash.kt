package com.example.traveler.activities

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import com.example.traveler.MainActivity
import com.example.traveler.R
import kotlinx.android.synthetic.main.activity_logo_splash.*
import kotlin.concurrent.thread

class LogoSplash : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logo_splash)
        logoImage.animate().alpha(0f).alphaBy(1f).duration = 4000
        thread(start = true) {
            Thread.sleep(7000)
            startActivity(MainActivity.getLaunchIntent(this))
        }
    }

    companion object {
        fun getLaunchIntent(from: Context) = Intent(from, LogoSplash::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
    }
}
