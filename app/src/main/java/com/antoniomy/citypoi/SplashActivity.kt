package com.antoniomy.citypoi

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        loadSplashScreen()
    }

    private fun loadSplashScreen() {
        val viewSplash = View.inflate(this, R.layout.activity_splash, null)
        setContentView(viewSplash)

        val anim = AlphaAnimation(0.1f, 1.0f)
        anim.duration = 3000
        viewSplash.animation = anim
        anim.setAnimationListener(
            object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {}
                override fun onAnimationRepeat(animation: Animation) {}
                override fun onAnimationEnd(animation: Animation) {
                }
            })

        findViewById<Button>(R.id.btn_start).setOnClickListener {
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
        }
    }
}