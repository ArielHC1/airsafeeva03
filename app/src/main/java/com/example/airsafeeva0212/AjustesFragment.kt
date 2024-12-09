package com.example.airsafeeva0212

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.example.airsafeeva0212.databinding.FragmentAjustesBinding
import com.example.airsafeeva0212.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class AjustesFragment : Fragment() {

    private var _binding: FragmentAjustesBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAjustesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializar FirebaseAuth y SharedPreferences
        auth = FirebaseAuth.getInstance()
        sharedPreferences = requireActivity().getSharedPreferences("LoginPrefs", AppCompatActivity.MODE_PRIVATE)

        // Configurar acción del botón "Cambiar Contraseña"
        binding.btnCambiar.setOnClickListener {
            val intent = Intent(requireContext(), CambiarContracenaActivity::class.java)
            startActivity(intent)
        }

        // Configurar acción del botón "Cerrar Sesión"
        binding.btnCerrar.setOnClickListener {
            showLogoutDialog()
        }
    }

    class AppCompatActivity {
        companion object {
            val MODE_PRIVATE: Int = 0
        }

    }

    private fun showLogoutDialog() {
        // Crear un diálogo de confirmación
        AlertDialog.Builder(requireContext())
            .setTitle("Cerrar Sesión")
            .setMessage("¿Estás seguro de que deseas cerrar sesión?")
            .setPositiveButton("Sí") { dialog, _ ->
                // Eliminar preferencia "Recuérdame"
                val editor = sharedPreferences.edit()
                editor.putBoolean("rememberMe", false)
                editor.apply()

                // Cerrar sesión en Firebase
                auth.signOut()

                // Redirigir al LoginActivity
                navigateToLogin()
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun navigateToLogin() {
        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        requireActivity().finish() // Cierra la actividad actual
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
