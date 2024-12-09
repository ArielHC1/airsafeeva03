package com.example.airsafeeva0212.ui.login

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.airsafeeva0212.MainActivity
import com.example.airsafeeva0212.R
import com.example.airsafeeva0212.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE)

        // Verificar si el usuario ya eligió "Recuérdame"
        if (sharedPreferences.getBoolean("rememberMe", false)) {
            navigateToMain()
        }

        binding.loginButton?.setOnClickListener {
            val correo = binding.email?.text.toString()
            val contrasena = binding.password.text.toString()
            val rememberMe = binding.checkboxRememberMe?.isChecked ?: false

            if (correo.isEmpty()) {
                binding.email?.error = "Ingrese su correo"
                return@setOnClickListener
            }
            if (contrasena.isEmpty()) {
                binding.password.error = "Ingrese su contraseña"
                return@setOnClickListener
            }
            iniciarSesion(correo, contrasena, rememberMe)
        }
    }

    private fun iniciarSesion(email: String, password: String, rememberMe: Boolean) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(this, "Inicio de sesión correcto", Toast.LENGTH_LONG).show()

                    // Guardar preferencia de "Recuérdame" si está seleccionada
                    val editor = sharedPreferences.edit()
                    editor.putBoolean("rememberMe", rememberMe)
                    editor.apply()

                    navigateToMain()
                } else {
                    Toast.makeText(this, "Error al Iniciar sesión", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish() // Cierra la actividad actual para evitar regresar al login
    }
}
