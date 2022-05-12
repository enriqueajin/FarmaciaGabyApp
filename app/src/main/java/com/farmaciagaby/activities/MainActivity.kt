package com.farmaciagaby.activities

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.*
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.farmaciagaby.R
import com.farmaciagaby.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawer: DrawerLayout
//    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var appBarConfiguration: AppBarConfiguration

//    option 2
    private lateinit var navController: NavController
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var listener: NavController.OnDestinationChangedListener

    private lateinit var binding: ActivityMainBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater);
        val view = binding.root
        setContentView(view);
//        setUpNavigationDrawer()
        setUpNavConfig()

    }

    private fun setUpNavigationDrawer() {
        drawer = findViewById(R.id.drawer_layout)
//        toggle = ActionBarDrawerToggle(this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close)

        val navController = findNavController(R.id.nav_host_fragment_container)
//        drawer.addDrawerListener(toggle)
        appBarConfiguration = AppBarConfiguration(navController.graph, drawer)
        val toolbar : Toolbar = findViewById(R.id.toolbar_main)
        setSupportActionBar(toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration);

        val navigationView : NavigationView = findViewById(R.id.nav_view)
        navigationView.setupWithNavController(navController)
        navigationView.setNavigationItemSelectedListener(this)

    }

    private fun setUpNavConfig() {
        navController = findNavController(R.id.nav_host_fragment_container)
        drawerLayout = findViewById(R.id.drawer_layout)
        val navigationView : NavigationView = findViewById(R.id.nav_view)
        navigationView.setupWithNavController(navController)
        navigationView.setNavigationItemSelectedListener(this)
        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)
        val toolbar : Toolbar = findViewById(R.id.toolbar_main)
        setSupportActionBar(toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_container);
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()

//        onBackPressed()
//        return true

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_item_request_quotation -> navController.navigate(R.id.request_quotation_graph)
            R.id.nav_item_two -> Log.d("TAG", "nav item two")
            R.id.nav_item_three -> Log.d("TAG", "nav item three")
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

//
//    override fun onPostCreate(savedInstanceState: Bundle?) {
//        super.onPostCreate(savedInstanceState)
//        toggle.syncState()
//    }
//
//    override fun onConfigurationChanged(newConfig: Configuration) {
//        super.onConfigurationChanged(newConfig)
//        toggle.onConfigurationChanged(newConfig)
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        if (toggle.onOptionsItemSelected(item)) {
//            return true
//        }
//        return super.onOptionsItemSelected(item)
//    }
}