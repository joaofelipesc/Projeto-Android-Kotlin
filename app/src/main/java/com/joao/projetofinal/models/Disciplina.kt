package com.joao.projetofinal.models

data class Disciplina(val id: Int,val nome: String, val descricao: String, val cargaHoraria: Int) {

    constructor() : this(0, "", "", 0)


    override fun toString(): String {
        return "Disciplina(nome='$nome', descricao='$descricao', cargaHoraria=$cargaHoraria)"
    }



}

