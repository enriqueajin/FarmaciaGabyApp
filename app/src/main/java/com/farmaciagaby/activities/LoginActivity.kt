package com.farmaciagaby.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.farmaciagaby.R
import com.farmaciagaby.databinding.ActivityLoginBinding
import com.farmaciagaby.network.FirebaseHelper

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setData()
    }

    private fun setData() {

    }
}