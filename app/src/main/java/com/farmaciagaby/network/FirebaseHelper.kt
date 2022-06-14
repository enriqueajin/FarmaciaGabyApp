package com.farmaciagaby.network

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

object FirebaseHelper {

    private lateinit var auth: FirebaseAuth

    fun getAuthentication(): FirebaseAuth {
        auth = Firebase.auth
        return auth
    }

    fun loggedOut() {
        auth.signOut()
    }
}