package za.co.superhero.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.setupActionBarWithNavController
import za.co.superhero.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    private fun setupNavigation() {
        val navController = findNavController(R.id.fragment)
        setupActionBarWithNavController(navController)

    }

    override fun onSupportNavigateUp() =
        findNavController(R.id.fragment).navigateUp()
}

