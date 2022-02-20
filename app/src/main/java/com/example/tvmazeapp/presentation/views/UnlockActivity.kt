package com.example.tvmazeapp.presentation.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.preference.PreferenceManager
import com.example.tvmazeapp.R
import com.example.tvmazeapp.databinding.ActivityUnlockBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView


@AndroidEntryPoint
class UnlockActivity : AppCompatActivity(), View.OnClickListener{

    private val MAX_LENGHT = 4
    private var user_pin = ""
    private var true_pin = ""
    private var attempts = 0

    private lateinit var binding : ActivityUnlockBinding
    private var dots = ArrayList<ImageView>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUnlockBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this /* Activity context */)
        val security = sharedPreferences.getBoolean("security_pin", false)
        true_pin = sharedPreferences.getString("pin", "") ?: ""

        binding.buttonClear.setOnClickListener {
            if (user_pin.isNotEmpty()) {
                user_pin = user_pin.substring(0, user_pin.length-1)

                setImagesState()
            }
        }

        binding.button0.setOnClickListener(this)
        binding.button1.setOnClickListener(this)
        binding.button2.setOnClickListener(this)
        binding.button3.setOnClickListener(this)
        binding.button4.setOnClickListener(this)
        binding.button5.setOnClickListener(this)
        binding.button6.setOnClickListener(this)
        binding.button7.setOnClickListener(this)
        binding.button8.setOnClickListener(this)
        binding.button9.setOnClickListener(this)

        dots.add(binding.dot1)
        dots.add(binding.dot2)
        dots.add(binding.dot3)
        dots.add(binding.dot4)
   }

    override fun onClick(v: View?) {
        v?.let {
            val number = getButtonNumber(v)
            Timber.d("User press number %s", number)

            user_pin+= number.toString()

            if (user_pin.length == MAX_LENGHT) {
                if (user_pin.equals(true_pin)) {
                    Timber.d("Right Pass")

                    val i = Intent(this, ShowsActivity::class.java)
                    startActivity(i)

                    finish()
                } else {
                    Timber.d("Wrong Pass")

                    attempts+=1
                    if (attempts>3) finish()

                    //vibrate the dots layout
                    shakeImages()
                }
            } else if (user_pin.length > MAX_LENGHT){
                user_pin = number.toString()
            }

            setImagesState()
        }
    }

    fun setImagesState(){
        for (i in 0 until user_pin.length) {
            dots.get(i).setImageResource(R.drawable.ic_catching_pokemon_24)
        }
        if (user_pin.length < 4) {
            for (j in user_pin.length..3) {
                dots.get(j).setImageResource(R.drawable.ic_circle_24)
            }
        }
    }

    fun shakeImages(){
        val shake: Animation = AnimationUtils.loadAnimation(this, R.anim.shake_animation)
        binding.dotLayout.startAnimation(shake)
        Toast.makeText(this, "Wrong Password", Toast.LENGTH_SHORT).show()
    }

    fun getButtonNumber(v:View): Int{
        return when(v.id){
            R.id.button0 -> 0
            R.id.button1 -> 1
            R.id.button2 -> 2
            R.id.button3 -> 3
            R.id.button4 -> 4
            R.id.button5 -> 5
            R.id.button6 -> 6
            R.id.button7 -> 7
            R.id.button8 -> 8
            R.id.button9 -> 9
            else -> 0
        }
    }
}