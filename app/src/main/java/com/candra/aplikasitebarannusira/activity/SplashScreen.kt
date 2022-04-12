package com.candra.aplikasitebarannusira.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.candra.aplikasitebarannusira.`object`.Animation
import com.candra.aplikasitebarannusira.databinding.SplashScreenActivityBinding

@SuppressLint("CustomSplashScreen")
class SplashScreen: AppCompatActivity()
{

    private lateinit var binding: SplashScreenActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SplashScreenActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        Animation.playAnimationSplashScreen(binding.imageView)

        Handler(mainLooper).postDelayed({
            startActivity(Intent(this@SplashScreen,MainUtama::class.java))
            finish()
        },5000)
    }
}