package com.joao.projetofinal

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.joao.projetofinal.databinding.ActivityMainBinding
import com.joao.projetofinal.databinding.FragmentSplashBinding


class SplashFragment : Fragment() {
    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return _binding!!.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Handler(Looper.getMainLooper()).postDelayed(this::checkAuth, 3000)
        auth = Firebase.auth
    }

    private fun checkAuth() {
        val rota = if (auth.currentUser == null) {
            binding.txtTextoBoasVindas.setText("APP DESENVOLVIDO POR João Felipe Silva Coromberk. RA:23319")
            R.id.action_splashFragment_to_loginFragment2

        } else {
            binding.txtTextoBoasVindas.setText("Bem Vindo" + auth.currentUser?.email+ "  APP DESENVOLVIDO POR João Felipe Silva Coromberk. RA:23319")
            R.id.action_splashFragment_to_mainFragment
        }
        Handler(Looper.getMainLooper()).postDelayed({
            findNavController().navigate(rota)
        }, 3000)
    }
}