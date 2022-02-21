package com.example.tvmazeapp.presentation.views

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tvmazeapp.R
import android.content.Intent
import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import androidx.preference.PreferenceManager
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    @Inject lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val security = sharedPreferences.getBoolean("security", false)
        Timber.d("security is : %s", security)

        Handler(Looper.getMainLooper()).postDelayed({

            if (security){
                val intent = Intent(this, UnlockActivity::class.java)
                startActivity(intent)
            }else{
                val i = Intent(this, ShowsActivity::class.java)
                startActivity(i)
            }
            finish()
        }, 1500)
    }
}