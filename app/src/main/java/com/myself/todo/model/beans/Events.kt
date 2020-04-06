package com.myself.todo.model.beans


class Events {
    var evento: String? = null
    var icon: String? = null
    var data_dia: String? = null
    var data_hora: String? = null
    var anotacao: String? = null
    var id: String? = null
    var complete: Boolean = false



    fun isAcreateEvent():Boolean{
      return  id.equals("createEvnt")
    }

    constructor()
    constructor(evento: String?, icon: String?, data_dia: String?, data_hora: String?, anotacao: String?) {
        this.evento = evento
        this.icon = icon
        this.data_dia = data_dia
        this.data_hora = data_hora
        this.anotacao = anotacao
    }

    companion object{
        fun createEvent():Events{
            val e = Events("Adicionar novo evento", "\uD83D\uDCDD", "", "", "")
            e.id = "createEvnt"
            return e
        }
    }
}