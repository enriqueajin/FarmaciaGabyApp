package com.farmaciagaby.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.farmaciagaby.R
import com.farmaciagaby.activities.MainActivity
import com.farmaciagaby.databinding.FragmentSplashBinding
import kotlinx.coroutines.*
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

class SplashFragment(override val coroutineContext: CoroutineContext) : Fragment(), CoroutineScope {

    private lateinit var binding: FragmentSplashBinding;
    private var context: Fragment = this;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSplashBinding.inflate(inflater, container, false);

//        try {
//            Thread.sleep(2000)
//        } catch (ex: Exception) {
//            println("Error: " + ex.printStackTrace());
//        }

        launch {
            delay(2500)
            NavHostFragment.findNavController(context).navigate(R.id.action_splashFragment_to_loginFragment);
        }

//        Navigation.findNavController(requireActivity(), R.id.splashFragment)
//            .navigate(R.id.action_splashFragment_to_loginFragment);

//        NavHostFragment.findNavController(this).navigate(R.id.action_splashFragment_to_loginFragment);
        return binding.root;
    }

}