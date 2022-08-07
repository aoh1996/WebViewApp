package com.example.webviewapp

import NetworkConnectionMonitor
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.webviewapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var prefs: SharedPreferences
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var networkConnectionMonitor: NetworkConnectionMonitor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navController = findNavController(R.id.nav_host_fragment_activity_main)
        prefs = getSharedPreferences("com.example.webviewapp", MODE_PRIVATE)
    }

    override fun onResume() {
        networkConnectionMonitor = (application as WebViewApp).networkConnectionMonitor
        networkConnectionMonitor.observe(this) { isConnected ->
            if (prefs.getBoolean("license_accepted", false)) {
                navController
                if (isConnected) {
                    navController.popBackStack()
                    navController.navigate(R.id.webViewFragment)
                } else {
                    navController.popBackStack()
                    navController.navigate(R.id.noInternetFragment)
                }
            } else {
                    navController.popBackStack()
                    navController.navigate(R.id.startFragment)

            }

        }
        super.onResume()
    }

//    override fun onSupportNavigateUp(): Boolean {
//        return Navigation.findNavController(this, R.id.nav_host_fragment_activity_main).navigateUp()
//                || super.onSupportNavigateUp()
//    }
}