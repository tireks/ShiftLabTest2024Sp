package com.tirexmurina.shiftlabtest2024sp.presentation

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import com.tirexmurina.shiftlabtest2024sp.R
import com.tirexmurina.shiftlabtest2024sp.presentation.home.HomeFragmentDirections
import com.tirexmurina.shiftlabtest2024sp.presentation.registration.RegistrationFragmentDirections
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val navController get() = findNavController(R.id.mainDataContainer)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun openAccount(){
        val action = RegistrationFragmentDirections.actionRegistrationFragmentToHomeFragment()
        navController.navigate(action)
    }

    fun closeAccount(){
        val action = HomeFragmentDirections.actionHomeFragmentToRegistrationFragment()
        navController.navigate(action)
    }

}