package com.farmaciagaby.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.Navigation
import com.farmaciagaby.R
import com.farmaciagaby.databinding.FragmentLoginBinding
import com.farmaciagaby.models.Detalle
import com.farmaciagaby.models.QuotationDetail
import com.farmaciagaby.network.FirebaseHelper
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

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

        var db = Firebase.firestore
//        val product = Product("Mi producto")
//        db.collection("cotizacion").add(product)

//        val products = db.collection("producto").get().addOnCompleteListener(requireActivity(), object : OnCompleteListener<QuerySnapshot?> {
//            override fun onComplete(task: Task<QuerySnapshot?>) {
//                val list = arrayListOf<String>()
//                for (doc in task.result!!) {
//                    list.add(doc.id)
//                }
//                Log.d("TAG", "onComplete: $list")
//            }
//        })
        val listOfMaps = arrayListOf<Map<String, Any>>()
        val productList = arrayListOf<Any?>()
        val products = db.collection("producto").get().addOnSuccessListener { documents ->
            documents.forEachIndexed { index, document ->
                listOfMaps.add(document.data)
                Log.d("TAG", "onCreateView: ")
                productList.add(document.data["nombre"])
            }
            val productArray = productList.toArray()

            val detalle = Detalle(
                "segunda cotización del año",
                Timestamp(Date()),
                "epfLJp58sC06YkewgmVj",
                "Jerausa",
                productList
            )
            val quotationDetails = QuotationDetail(detalle)
//            val cotizaciones = db.collection("cotizacion").add(detalle)

        }
    }

    private fun setData() {

        binding.btnSignIn.setOnClickListener { view ->
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString()
            val auth = FirebaseHelper.getAuthentication()

            if (validateEmail(email) && validate(password)) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(requireActivity()) { task ->
                        if (task.isSuccessful) {
                            // Sign in success
                            Log.d("TAG", "signInWithEmail:success")
                            val user = auth.currentUser
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