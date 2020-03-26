package com.myself.todo.Beans


class Events {
    var evento: String? = null
    var descricao: String? = null
    private var UserID: String? = null
    private var data: String? = null
    var id: String? = null
    var favorite = false
    var tasks:ArrayList<Tarefas>? = null




    constructor()
    constructor(evento: String, descricao: String, UserID: String, data: String, id: String, favorite: Boolean,tarefas: ArrayList<Tarefas>) {
        this.evento = evento
        this.descricao = descricao
        this.UserID = UserID
        this.data = data
        this.id = id
        this.favorite = favorite
    }

}