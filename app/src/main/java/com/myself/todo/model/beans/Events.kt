package com.myself.todo.model.beans


class Events {
    var icon: String? = null
    var evento: String? = null
    var anotacao: String? = null
    var userID: String? = null
    var data_dia: String? = null
    var data_hora: String? = null
    var id: String? = null
    var favorite: Boolean = false



    fun isAcreateEvent():Boolean{
      return  id.equals("createEvnt")
    }

    constructor()
    constructor(evento: String, anotacao: String?, UserID: String, data_dia: String, data_hora: String, id: String?, favorite: Boolean) {
        this.evento = evento
        this.anotacao = anotacao
        this.userID = UserID
        this.data_dia = data_dia
        this.data_hora = data_hora
        this.id = id
        this.favorite = favorite
    }

    companion object{
        fun createEvent():Events{
            val e = Events()
            e.id = "createEvnt"
            e.icon = "\uD83D\uDCDD"
            return e
        }
    }
}