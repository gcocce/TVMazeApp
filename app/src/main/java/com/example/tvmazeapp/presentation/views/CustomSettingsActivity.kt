package com.example.tvmazeapp.presentation.views

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.example.tvmazeapp.databinding.ActivityCustomSettingsBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject
import android.widget.CompoundButton
import android.widget.Toast
import com.example.tvmazeapp.utils.Security

@AndroidEntryPoint
class CustomSettingsActivity : AppCompatActivity() {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private lateinit var binding : ActivityCustomSettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCustomSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val security = sharedPreferences.getBoolean("security", false)
        val truePin = sharedPreferences.getString("pin", "") ?: ""

        if(!security){
            disableAll()
        }else{
            binding.switchSecurity.isChecked = true
        }

        if (truePin!=""){
            binding.etPin.setText("****")
        }

        binding.switchSecurity.setOnCheckedChangeListener { _, isChecked ->
            binding.etPin.setText("")
            if (isChecked) {
                enableAll()
            } else {
                disableAll()
            }
        }

        binding.btSave.setOnClickListener {
            val pin = binding.etPin.text.toString()
            val newSecurity = binding.switchSecurity.isChecked

            val editor = sharedPreferences.edit()
            editor.putBoolean("security", newSecurity)

            if (newSecurity){
                if (pin.length == 4){
                    editor.apply {
                        putBoolean("security", binding.switchSecurity.isChecked)
                        if (pin!="****") putString("pin", Security.md5(pin))
                        Timber.d("Security pin is : %s", Security.md5(pin))
                        apply()
                    }
                    super.onBackPressed()
                }else{
                    Toast.makeText(this, "PIN sould have at least 4 digits", Toast.LENGTH_LONG).show()
                }
            }else{
                editor.apply()
                super.onBackPressed()
            }
        }
    }

    fun disableAll(){
        binding.etPin.isEnabled = false
        if (binding.switchFingerPrint.visibility == View.VISIBLE){
            binding.switchFingerPrint.isEnabled = false
        }
    }

    fun enableAll(){
        binding.etPin.isEnabled = true
        if (binding.switchFingerPrint.visibility == View.VISIBLE){
            binding.switchFingerPrint.isEnabled = true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                super.onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}