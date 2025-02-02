package com.farmaciagaby.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.*
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.farmaciagaby.R
import com.farmaciagaby.databinding.ActivityMainBinding
import com.farmaciagaby.network.FirebaseHelper
import com.farmaciagaby.viewmodels.EmployeesViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.navigation.NavigationView
import com.google.gson.GsonBuilder
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawer: DrawerLayout
//    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var appBarConfiguration: AppBarConfiguration

//    option 2
    private lateinit var navController: NavController
    private lateinit var drawerLayout: DrawerLayout

    private lateinit var binding: ActivityMainBinding;
    private val gson = GsonBuilder().create()
    private val employeesViewModel: EmployeesViewModel by viewModels()
    private val context = this

    override fun onStart() {
        super.onStart()
        val currentUser = FirebaseHelper.getAuthentication().currentUser
        if (currentUser != null) {
            Log.d("TAG", "onStart: you are logged in")
        } else {
            Log.d("TAG", "onStart: you are not logged in")
        }
    }

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

        // Get user uid from Shared Preferences
        val sharedPref = getSharedPreferences(
            resources.getString(R.string.preference_file_key),
            Context.MODE_PRIVATE
        )
        val userUid = sharedPref?.getString(
            getString(R.string.uid_key),
            "uid"
        )

        lifecycleScope.launch {

            // Get user logged
            employeesViewModel.getEmployeeByUid(userUid)
            employeesViewModel.employeeData.observe(context) { employeeResponse ->
                Log.d("TAG", "observing employee from activity : $employeeResponse")

                // Set navigation drawer user name
                navigationView.getHeaderView(0).findViewById<TextView>(R.id.tv_nav_header).text = "Bienvenido(a), ${employeeResponse.employee.primer_nombre} ${employeeResponse.employee.primer_apellido}"
            }
        }

        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)
        val toolbar : Toolbar = findViewById(R.id.toolbar_main)
        setSupportActionBar(toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)

        // Set title to action bar
        supportActionBar?.title = resources.getString(R.string.action_bar_quotation_history)

        findViewById<TextView>(R.id.tv_logout).setOnClickListener {
            showSignOutDialog()
        }
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
            R.id.nav_item_manage_products -> navController.navigate(R.id.manage_products_graph)
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun showSignOutDialog() {
        val dialog = MaterialAlertDialogBuilder(this)
            .setTitle("Cerrar sesión")
            .setMessage("¿Está seguro que desea cerrar sesión?")
            .setPositiveButton("Aceptar") { dialogInterface, i ->
                FirebaseHelper.loggedOut()
                val navHostFragment = supportFragmentManager.findFragmentById(androidx.navigation.fragment.R.id.nav_host_fragment_container) as NavHostFragment
                val navController = navHostFragment.navController
                navController.navigate(R.id.action_main_to_login_activity)
                finish()
            }
            .setNegativeButton("Cancelar") { dialogInterface, i -> }
            .show()
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