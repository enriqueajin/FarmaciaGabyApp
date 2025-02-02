package com.farmaciagaby.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.Navigation
import com.farmaciagaby.R
import com.farmaciagaby.databinding.FragmentLoginBinding
import com.farmaciagaby.network.FirebaseHelper
import com.google.android.material.snackbar.Snackbar

class LoginFragment : BaseFragment() {

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Close app when user press back button in Login Fragment
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                activity!!.finish()
            }
        })
    }

    private fun setData() {

        binding.btnSignIn.setOnClickListener { view ->
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString()
            val auth = FirebaseHelper.getAuthentication()

            if (validateEmail(email) && validate(password)) {
                showLoadingDialog()

                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(requireActivity()) { task ->
                    hideLoadingDialog()
                    if (task.isSuccessful) {
                        // Sign in successful
                        Log.d("TAG", "signInWithEmail:success")
                        val user = auth.currentUser

                        // Store user uid in Shared Preferences
                        val sharedPref = activity?.getSharedPreferences(
                            resources.getString(R.string.preference_file_key),
                            Context.MODE_PRIVATE
                        )
                        with (sharedPref?.edit()) {
                            this?.putString(getString(R.string.uid_key), user?.uid)
                            this?.apply()
                        }

                        Navigation.findNavController(view).navigate(R.id.action_login_to_main_activity)
                        requireActivity().finish()

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("TAG", "signInWithEmail:failure", task.exception)
                        Snackbar.make(
                            binding.fragmentLoginContainer,
                            "Correo electrónico y/o contraseña incorrecto.",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }
            } else {
                Snackbar.make(
                    binding.fragmentLoginContainer,
                    "Por favor, ingrese su usuario y contraseña",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

        // Navigate to reset password fragment
        binding.tvResetPassword.setOnClickListener { view ->
            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_resetPasswordFragment)
        }
    }
}