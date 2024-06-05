package com.joao.projetofinal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.joao.projetofinal.databinding.FragmentMainBinding


class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializa a autenticação do Firebase
        auth = FirebaseAuth.getInstance()

        // Define a Toolbar como a ActionBar do Fragment
        setHasOptionsMenu(true)

        // Define o comportamento dos botões do menu
        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.disciplinas -> {
                    // Navegue para a tela de disciplinas
                    findNavController().navigate(R.id.action_mainFragment_to_disciplinasFragment)

                    true
                }
                R.id.mapsscreen -> {
                    // Navegue para a tela do mapa
                    findNavController().navigate(R.id.action_mainFragment_to_mapsFragment)
                    Toast.makeText(requireContext(), "Mapa", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.signout -> {
                    // Desloga o usuário
                    auth.signOut()
                    // Navegue para a tela de login
                    findNavController().navigate(R.id.action_mainFragment_to_loginFragment2)
                    true
                }
                else -> false
            }
        }
    }

    // Infla o menu do Fragment
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_toolbar_main_fragment, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    // Libera os recursos do Fragment
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}