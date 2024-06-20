package com.rentby.rentbymobile.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.rentby.rentbymobile.R
import com.rentby.rentbymobile.databinding.ActivitySplashscreenBinding
import com.rentby.rentbymobile.ui.main.MainActivity

class SplashscreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashscreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashscreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Change the status bar and navigation bar colors
        window.statusBarColor = resources.getColor(R.color.rentby_primary, theme)
        window.navigationBarColor = resources.getColor(R.color.rentby_primary, theme)

        // Load the animation
        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)

        // Start the animation
        binding.logo.startAnimation(fadeIn)

        // Handler to delay the transition to the main activity
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000) // 3 seconds delay
    }
}
