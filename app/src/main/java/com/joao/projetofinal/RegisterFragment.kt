package com.joao.projetofinal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.joao.projetofinal.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater,container,false)
        return _binding!!.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth
        binding.btnRegistro.setOnClickListener {
            val email = binding.editTextEmail.text.toString()
            val senha1 = binding.editTextSenha.text.toString()
            val senha2 = binding.editTextConfirmarSenha.text.toString()

            if(email.isEmpty() || senha1.isEmpty() || senha2.isEmpty() ){
                Toast.makeText(activity, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            }
            if(senha1 != senha2){
                Toast.makeText(activity, "As senhas não coincidem", Toast.LENGTH_SHORT).show()
            }

            auth.createUserWithEmailAndPassword(email,senha1).addOnCompleteListener {task->
                if(task.isSuccessful){
                    findNavController().navigate(R.id.action_registerFragment2_to_splashFragment)
                }
                else{
                    Toast.makeText(activity, task.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }

        }


    }




}