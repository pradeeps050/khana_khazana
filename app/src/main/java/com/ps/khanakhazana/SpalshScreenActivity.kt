package com.ps.khanakhazana

import androidx.appcompat.app.AppCompatActivity
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import com.ps.khanakhazana.ads.AppOpenManager
import com.ps.khanakhazana.databinding.ActivitySpalshScreenBinding
import com.ps.khanakhazana.utils.RecipeApplication

private const val LOG_TAG = "SplashActivity"

class SpalshScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySpalshScreenBinding
    private  val COUNTER_TIME = 5L
    private var secondsRemaining: Long = 0L


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySpalshScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        Handler().postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000) // 3000 is the delayed time in milliseconds.
    }

    /*private fun createTimer(seconds: Long) {
        val counterTextView: TextView = findViewById(R.id.timer)
        val countDownTimer: CountDownTimer = object : CountDownTimer(seconds * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                secondsRemaining = millisUntilFinished / 1000 + 1
                counterTextView.text = "App is done loading in: $secondsRemaining"
            }

            override fun onFinish() {
                secondsRemaining = 0
                counterTextView.text = "Done."

                val application = application as? RecipeApplication

                // If the application is not an instance of MyApplication, log an error message and
                // start the MainActivity without showing the app open ad.
                if (application == null) {
                    Log.e(LOG_TAG, "Failed to cast application to MyApplication.")
                    startMainActivity()
                    return
                }

                var appOpenManager = AppOpenManager(application)
                appOpenManager.showAdIfAvailable()

            }
        }
        countDownTimer.start()
    }

    *//** Start the MainActivity. *//*
    fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }*/
}