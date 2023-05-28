package com.antoniomy.citypoi

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        loadSplashScreen()
    }

    private fun loadSplashScreen() {
        // SET activity_splash.xml as layout
        val viewSplash = View.inflate(this, R.layout.activity_splash, null)
        setContentView(viewSplash)

        // Gradient Animation
        val anim = AlphaAnimation(0.1f, 1.0f) // change alpha from 0.5 to 1.0
        anim.duration = 3000 // animate in 5000ms
        viewSplash.animation = anim
        anim.setAnimationListener(
            object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {}
                override fun onAnimationRepeat(animation: Animation) {}
                override fun onAnimationEnd(animation: Animation) {
                }
            })

        findViewById<Button>(R.id.btn_start).setOnClickListener {
            // redirect to the other screen, such as MainActivity
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(intent)

            // close SplashActivity
            finish()
        }
    }
}