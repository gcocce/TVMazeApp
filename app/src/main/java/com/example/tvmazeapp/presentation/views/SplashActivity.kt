package com.example.tvmazeapp.presentation.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tvmazeapp.R
import android.content.Intent
import android.os.Handler
import android.os.Looper
import androidx.preference.PreferenceManager
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this /* Activity context */)
        val security = sharedPreferences.getBoolean("security_pin", false)

        Timber.d("security is : %s", security)

        Handler(Looper.getMainLooper()).postDelayed({

            if (security){
                val intent = Intent(this, UnlockActivity::class.java)
                startActivity(intent)
            }else{
                val i = Intent(this, ShowsActivity::class.java)
                startActivity(i)
            }
            // close this activity
            finish()
        }, 1500)
    }
}