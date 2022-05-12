package com.farmaciagaby.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.Navigation
import com.farmaciagaby.R
import com.farmaciagaby.databinding.FragmentResetPasswordBinding
import com.google.android.material.button.MaterialButton

class ResetPasswordFragment : Fragment() {

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
        setUpResetPasswordAlert()
        return binding.root
    }

    private fun setUpResetPasswordAlert() {
        val view = layoutInflater.inflate(R.layout.reset_password_alert, null)
        val okButton = view.findViewById<MaterialButton>(R.id.btnOk)
        val alertDialog = AlertDialog.Builder(requireActivity()).create()
        alertDialog.setView(view)
        alertDialog.setCancelable(false);

        binding.btnSend.setOnClickListener {
            alertDialog.show()
        }

        alertDialog.setOnDismissListener {
            it.dismiss()
        }

        okButton.setOnClickListener {
            alertDialog.dismiss();
            Navigation.findNavController(binding.root).navigate(R.id.action_resetPasswordFragment_to_loginFragment);
        }

    }
}