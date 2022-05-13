package com.farmaciagaby.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import com.farmaciagaby.R
import com.farmaciagaby.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        setData()
        return binding.root
    }

    private fun setData() {
        // Hide action bar
//        (activity as AppCompatActivity).supportActionBar?.hide()

        // Set focus to email EditText
        binding.etEmail.requestFocus()

        binding.btnSignIn.setOnClickListener { view ->
            Navigation.findNavController(view).navigate(R.id.action_login_to_main_activity)
        }

        // Navigate to reset password fragment
        binding.tvResetPassword.setOnClickListener { view ->
            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_resetPasswordFragment)
        }
    }
}