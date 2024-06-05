package com.joao.projetofinal

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.joao.projetofinal.databinding.ItemDisciplinaBinding
import com.joao.projetofinal.models.Disciplina

class DisciplinaAdapter(
    var disciplinas: MutableList<Disciplina>,
    private val onEditClick: (Disciplina) -> Unit,
    private val onDeleteClick: (Disciplina) -> Unit
) : RecyclerView.Adapter<DisciplinaAdapter.DisciplinaViewHolder>() {

    inner class DisciplinaViewHolder(private val binding: ItemDisciplinaBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(disciplina: Disciplina, position: Int) {
            binding.nomeTextView.text = disciplina.nome
            binding.descricaoTextView.text = disciplina.descricao
            binding.cargaHorariaTextView.text = "Carga Horária: ${disciplina.cargaHoraria}h"

            binding.editButton.setOnClickListener { onEditClick(disciplina) }
            binding.deleteButton.setOnClickListener { onDeleteClick(disciplina) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DisciplinaViewHolder {
        val binding = ItemDisciplinaBinding.inflate(            LayoutInflater.from(parent.context), parent, false)
        return DisciplinaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DisciplinaViewHolder, position: Int) {
        holder.bind(disciplinas[position], position) // Passa position aqui
    }

    override fun getItemCount(): Int = disciplinas.size
}