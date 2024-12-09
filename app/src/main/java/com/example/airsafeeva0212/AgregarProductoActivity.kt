package com.example.airsafeeva0212

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.airsafeeva0212.databinding.ActivityAgregarProductoBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AgregarProductoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAgregarProductoBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Configurar View Binding
        binding = ActivityAgregarProductoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ajustar insets para el diseño
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializar Firebase Database
        database = FirebaseDatabase.getInstance().getReference("sensores")

        // Datos para el Spinner
        val sensoresDeAire = listOf("Sensor Air X100", "Sensor Air Y200", "Sensor Air Z300")

        // Configurar el Spinner con estilos personalizados
        val spinnerAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, // Usar diseño predeterminado para simplicidad
            sensoresDeAire
        )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) // Estilo del menú desplegable
        binding.spinnerSensorAire.adapter = spinnerAdapter

        // Configurar botón para guardar el sensor seleccionado
        binding.btnGuardarDispositivo.setOnClickListener {
            val sensorSeleccionado = binding.spinnerSensorAire.selectedItem.toString()
            val nombreIngresado = binding.nombreSensorEditText.text.toString() // Obtiene el texto del EditText

            if (nombreIngresado.isEmpty()) {
                Toast.makeText(this, "Por favor ingresa el nombre del dispositivo", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            guardarSensorEnFirebase(sensorSeleccionado, nombreIngresado)
        }

        // Configurar botón para volver al inicio
        binding.volverInicioBTN.setOnClickListener {
            goToHome()
        }
    }

    private fun guardarSensorEnFirebase(sensor: String, nombre: String) {
        // Generar un ID único para el sensor
        val sensorId = database.push().key ?: return

        // Crear un mapa con los datos del sensor
        val sensorData = mapOf(
            "id" to sensorId,
            "modelo" to sensor,
            "nombre" to nombre
        )

        // Guardar en Firebase
        database.child(sensorId).setValue(sensorData)
            .addOnSuccessListener {
                // Mostrar alerta de éxito
                mostrarAlertaExito()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al guardar el sensor", Toast.LENGTH_SHORT).show()
            }
    }

    private fun mostrarAlertaExito() {
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("¡Éxito!")
        builder.setMessage("El sensor se agregó correctamente. ¿Qué deseas hacer?")

        builder.setPositiveButton("Agregar otro") { dialog, _ ->
            dialog.dismiss()
            // Limpiar el campo de texto para un nuevo sensor
            binding.nombreSensorEditText.text.clear()
            binding.spinnerSensorAire.setSelection(0) // Reinicia el Spinner al primer elemento
        }

        builder.setNegativeButton("Volver al inicio") { dialog, _ ->
            dialog.dismiss()
            // Redirigir al inicio
            goToHome()
        }

        builder.create().show()
    }

    private fun goToHome() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish() // Cierra la actividad actual
    }
}
