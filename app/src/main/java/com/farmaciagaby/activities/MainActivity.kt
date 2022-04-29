package com.farmaciagaby.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.navigation.*
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.farmaciagaby.R
import com.farmaciagaby.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding;
    private lateinit var navController: NavController;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater);
        val view = binding.root
        setContentView(view);
        setupActionBarWithNavController(findNavController(R.id.nav_host_fragment_container));

//        setStartDestination();
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_container);
        return navController.navigateUp() || super.onSupportNavigateUp();
    }
    private fun setStartDestination() {
//        val navHost: NavHostFragment = (supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container)) as NavHostFragment;
//        navController = navHost.navController;
//        val navInflater : NavInflater = navController.navInflater;
//        val graph : NavGraph = navInflater.inflate(R.navigation.login_graph);
//        navController.setGraph(graph);

//        val navHostFragment: NavHostFragment =
//            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment;
//        navController = navHostFragment.navController;
//        navHostFragment.navController.apply {
//            graph = createGraph(
//                startDestination = R.id.loginFragment.toString(),
//                route = R.id.login_graph.toString(),
//                builder
//            ) { }
//            navController.setGraph(graph.id)
//        }
    }
}