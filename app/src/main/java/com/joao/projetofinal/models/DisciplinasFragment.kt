package com.joao.projetofinal.models

import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.joao.projetofinal.DisciplinaAdapter
import com.joao.projetofinal.R
import com.joao.projetofinal.databinding.FragmentDisciplinasBinding

class DisciplinasFragment : Fragment() {

    private lateinit var binding: FragmentDisciplinasBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var disciplinasRef: DatabaseReference
    private lateinit var adapter: DisciplinaAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDisciplinasBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configurar Toolbar
        val toolbar: Toolbar = view.findViewById(R.id.toolbar)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        setHasOptionsMenu(true)

        database = FirebaseDatabase.getInstance()
        disciplinasRef = database.getReference("disciplinas")

        adapter = DisciplinaAdapter(mutableListOf(),
            onEditClick = { disciplina ->
                mostrarDialogoEditarDisciplina(disciplina)
            },
            onDeleteClick = { disciplina ->
                excluirDisciplinaNoFirebase(disciplina)
            }
        )
        binding.recyclerViewDisciplinas.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewDisciplinas.adapter = adapter

        disciplinasRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val disciplinas = mutableListOf<Disciplina>()
                for (childSnapshot in snapshot.children) {
                    val disciplina = childSnapshot.getValue(Disciplina::class.java)
                    disciplina?.let { disciplinas.add(it) }
                }
                adapter.disciplinas.clear()
                adapter.disciplinas.addAll(disciplinas)
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Erro: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_disciplinas, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_mapa -> {
                findNavController().navigate(R.id.action_disciplinasFragment_to_mapsFragment)
                true
            }
            R.id.menu_deslogar -> {
                Firebase.auth.signOut()
                findNavController().navigate(R.id.action_disciplinasFragment_to_loginFragment2)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun mostrarDialogoEditarDisciplina(disciplina: Disciplina) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_editar_disciplina, null)
        val builder = AlertDialog.Builder(requireContext())
            .setView(dialogView)
        val alertDialog = builder.create()

        val editTextNome = dialogView.findViewById<EditText>(R.id.editTextNome)
        val editTextDescricao = dialogView.findViewById<EditText>(R.id.editTextDescricao)
        val editTextCargaHoraria = dialogView.findViewById<EditText>(R.id.editTextCargaHoraria)

        editTextNome.setText(disciplina.nome)
        editTextDescricao.setText(disciplina.descricao)
        editTextCargaHoraria.setText(disciplina.cargaHoraria.toString())

        dialogView.findViewById<Button>(R.id.btnCancelar).setOnClickListener {
            alertDialog.dismiss()
        }
        dialogView.findViewById<Button>(R.id.btnSalvar).setOnClickListener {
            val nome = editTextNome.text.toString()
            val descricao = editTextDescricao.text.toString()
            val cargaHoraria = editTextCargaHoraria.text.toString().toIntOrNull() ?: 0

            val disciplinaAtualizada = Disciplina(disciplina.id, nome, descricao, cargaHoraria)
            atualizarDisciplinaNoFirebase(disciplinaAtualizada)
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    private fun atualizarDisciplinaNoFirebase(disciplinaAtualizada: Disciplina) {
        val disciplinasRef = database.getReference("disciplinas")
        var idreal = disciplinaAtualizada.id - 1
        disciplinasRef.child(idreal.toString()).setValue(disciplinaAtualizada)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Disciplina atualizada com sucesso!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { error ->
                Toast.makeText(requireContext(), "Erro ao atualizar disciplina: ${error.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun excluirDisciplinaNoFirebase(disciplina: Disciplina) {
        val disciplinasRef = database.getReference("disciplinas")
        var idreal = disciplina.id - 1
        disciplinasRef.child(idreal.toString()).removeValue()
            .addOnSuccessListener {
                val position = adapter.disciplinas.indexOf(disciplina)
                if (position != -1) {
                    adapter.disciplinas.removeAt(position)
                    adapter.notifyItemRemoved(position)
                }
                Toast.makeText(requireContext(), "Disciplina excluída com sucesso!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { error ->
                Toast.makeText(requireContext(), "Erro ao excluir disciplina: ${error.message}", Toast.LENGTH_SHORT).show()
            }
    }
}