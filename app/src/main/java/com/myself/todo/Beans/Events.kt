package com.myself.todo.Beans


class Events {
    var evento: String? = null
    var descricao: String? = null
    var UserID: String? = null
    var data: String? = null
    var id: String? = null
    var favorite = false
    var tasks:ArrayList<Tarefas> = ArrayList()



    fun isAcreateEvent():Boolean{
      return  id.equals("createEvnt")
    }

    constructor()
    constructor(evento: String, descricao: String, UserID: String, data: String, id: String, favorite: Boolean,tarefas: ArrayList<Tarefas>) {
        this.evento = evento
        this.descricao = descricao
        this.UserID = UserID
        this.data = data
        this.id = id
        this.favorite = favorite
        this.tasks = tarefas
    }

    companion object{
        fun createEvent():Events{
            val e = Events()
            e.id = "createEvnt"
            return e
        }
    }
}