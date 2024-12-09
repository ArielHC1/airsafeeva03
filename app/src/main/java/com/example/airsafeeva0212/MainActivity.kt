package com.example.airsafeeva0212

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_barra_de_navegacion)


        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.historial -> {
                    loadFragment(Fragment_Historial())
                    true
                }
                R.id.programar -> {
                    loadFragment(Fragment_Programar())
                    true
                }
                R.id.item_home -> {
                    loadFragment(DispositivosFragment())
                    true
                }
                R.id.ajustes -> {
                    loadFragment(AjustesFragment())
                    true
                }
                else -> false
            }
        }


        // Cargar el fragmento inicial al iniciar la actividad
        if (savedInstanceState == null) {
            bottomNavigationView.selectedItemId = R.id.item_home
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    }
