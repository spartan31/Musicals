package com.example.musicals.views.observer

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.example.musicals.R
import com.example.musicals.databinding.ActivitySplashScreenBinding
import com.example.musicals.views.activity.HomeScreen
import com.example.musicals.views.activity.LoginScreen
import com.example.musicals.views.activity.SplashScreen

class SplashScreenObserver(
    val binding: ActivitySplashScreenBinding, val splashScreen: SplashScreen
) : DefaultLifecycleObserver {
    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)


        val anim = AnimationUtils.loadAnimation(splashScreen, R.anim.splash_logo_rotate)
        val anim2 = AnimationUtils.loadAnimation(splashScreen, R.anim.splash_logo_inner)

        with(binding) {
            splashLogo.startAnimation(anim)
            splashInnerLogo.startAnimation(anim2)
            appTitleSplash.startAnimation(anim2)
        }


        Handler(Looper.getMainLooper()).postDelayed({
            val share = splashScreen.getSharedPreferences("login", AppCompatActivity.MODE_PRIVATE)
            val isLogin = share.getBoolean("flag", false)
            if (isLogin) {
                val intent = Intent(splashScreen, HomeScreen::class.java)
                splashScreen.startActivity(intent)
            } else {
                val intent = Intent(splashScreen, LoginScreen::class.java)
                splashScreen.startActivity(intent)
            }
            splashScreen.finish()
        }, 5000)
    }


}