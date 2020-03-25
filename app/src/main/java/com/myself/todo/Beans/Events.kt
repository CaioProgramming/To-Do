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
    constructor(evento: String, descricao: String, UserID: String, data: String, id: String, complete: Boolean, favorite: Boolean) {
        this.evento = evento
        this.descricao = descricao
        this.UserID = UserID
        this.data = data
        this.id = id
        this.complete = complete
        this.favorite = favorite
    }

}