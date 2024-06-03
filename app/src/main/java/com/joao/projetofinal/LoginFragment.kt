package com.joao.projetofinal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.joao.projetofinal.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root // Retorna a View raiz do binding
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth

        binding.btnEntrar.setOnClickListener {
            val email = binding.editTextEmail.text.toString()
            val senha = binding.editTextPassword.text.toString()

            if (email.isNotEmpty() && senha.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email,senha).addOnCompleteListener{task ->
                    if(task.isSuccessful){
                        Toast.makeText(context,"Login realizado com sucesso",Toast.LENGTH_SHORT).show()
                        Navigation.findNavController(view).navigate(R.id.action_loginFragment2_to_mainFragment)

                    }
                    else{
                        Toast.makeText(context,task.exception.toString(),Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }

        binding.btnRegistrar.setOnClickListener{
            Navigation.findNavController(view).navigate(R.id.action_loginFragment2_to_registerFragment2)
        }
    }
}