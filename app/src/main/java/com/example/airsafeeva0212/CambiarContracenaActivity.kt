package com.example.airsafeeva0212

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.airsafeeva0212.databinding.ActivityCambiarContracenaBinding
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth

class CambiarContracenaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCambiarContracenaBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCambiarContracenaBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        // Configuración de las vistas
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializar Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Configurar acción del botón "Actualizar"
        binding.btnActualizar.setOnClickListener {
            val user = auth.currentUser

            // Obtener los datos del formulario
            val passActual = binding.password.text.toString()
            val passNueva = binding.newpassword.text.toString()
            val passNueva2 = binding.newpassword2.text.toString()

            // Validar que los campos no sean nulos
            if (passActual.isEmpty() || passNueva.isEmpty() || passNueva2.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            // Validar que las nuevas contraseñas coincidan
            if (passNueva != passNueva2) {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            // Reautenticar al usuario actual
            val credential = EmailAuthProvider.getCredential(user!!.email.toString(), passActual)
            user.reauthenticate(credential).addOnCompleteListener { reauthTask ->
                if (reauthTask.isSuccessful) {
                    // Actualizar la contraseña
                    user.updatePassword(passNueva).addOnCompleteListener { updateTask ->
                        if (updateTask.isSuccessful) {
                            Toast.makeText(this, "Contraseña actualizada correctamente", Toast.LENGTH_LONG).show()
                            // Navegar al Home
                            goToHome()
                        } else {
                            Toast.makeText(this, "Error al actualizar la contraseña", Toast.LENGTH_LONG).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Autenticación fallida: Verifica tu contraseña actual", Toast.LENGTH_LONG).show()
                }
            }
        }

        // Configurar acción del botón "Volver a Inicio"
        binding.volverInicioBTN.setOnClickListener {
            goToHome()
        }
    }

    private fun goToHome() {
        val intent = Intent(this, MainActivity::class.java) // Reemplaza HomeActivity con el nombre de tu actividad Home
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish() // Cierra la actividad actual para evitar que el usuario regrese aquí con el botón de retroceso
    }
}
