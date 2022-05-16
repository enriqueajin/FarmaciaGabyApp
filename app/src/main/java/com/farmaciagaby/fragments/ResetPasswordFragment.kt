package com.farmaciagaby.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.farmaciagaby.R
import com.farmaciagaby.databinding.FragmentResetPasswordBinding
import com.farmaciagaby.network.FirebaseHelper
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar

class ResetPasswordFragment : BaseFragment() {

    private lateinit var binding: FragmentResetPasswordBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentResetPasswordBinding.inflate(layoutInflater);
        (activity as AppCompatActivity).supportActionBar?.show()
        binding.etEmail.requestFocus()
        setData()
        return binding.root
    }

    private fun setData() {
        binding.btnSend.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()

            if (validateEmail(email)) {
                showProgressIndicator()
                sendResetPasswordEmail(email)
            } else {
                Snackbar.make(
                    binding.fragmentResetPassContainer,
                    "Por favor ingrese un correo elecrónico válido.",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun showResetPasswordAlert(@StringRes text: Int, isSuccessful: Boolean) {
        val view = layoutInflater.inflate(R.layout.reset_password_alert, null)
        val okButton = view.findViewById<MaterialButton>(R.id.btnOk)
        val resetPasswordTitle = view.findViewById<TextView>(R.id.tv_reset_password)
        resetPasswordTitle.text = resources.getString(text)
        val alertDialog = AlertDialog.Builder(requireActivity()).create()
        alertDialog.setView(view)
        alertDialog.setCancelable(false);

        alertDialog.setOnDismissListener {
            it.dismiss()
        }

        okButton.setOnClickListener {
            alertDialog.dismiss();

            if (isSuccessful) {
                Navigation.findNavController(binding.root).navigate(R.id.action_resetPasswordFragment_to_loginFragment);
            }
        }

        alertDialog.show()
    }

    private fun sendResetPasswordEmail(email: String) {
        val auth = FirebaseHelper.getAuthentication()
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener(requireActivity(), OnCompleteListener { task ->
                if (task.isSuccessful) {
                    showResetPasswordAlert(R.string.reset_password_email_sent, true)
                } else {
                    showResetPasswordAlert(R.string.reset_password_email_sent_failed, false)
                }
                hideProgressIndicator()
            })
    }
}