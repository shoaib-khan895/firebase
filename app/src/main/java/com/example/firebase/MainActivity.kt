package com.example.firebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    companion object {
        const val MY_PREFERENCES = "login_shared_pref"
        const val NAME_KEY = "user_full_name"
        const val DESCRIPTION_KEY = "description_text"
    }
}