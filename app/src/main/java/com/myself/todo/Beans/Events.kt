package com.myself.todo.Beans

import org.junit.runner.RunWith

class Events {
    private var evento: String? = null
    var descricao: String? = null
    private var UserID: String? = null
    private var data: String? = null
    private var id: String? = null
    private var status: String? = null

    constructor(id: String?, evento: String?, descricao: String?, usuario: String?, data: String?, status: String?) {
        this.evento = evento
        UserID = usuario
        this.data = data
        this.id = id
        this.descricao = descricao
        this.status = status
    }

    constructor() {}

    fun getId(): String? {
        return id
    }

    fun setId(id: String?) {
        this.id = id
    }

    fun getEvento(): String? {
        return evento
    }

    fun setEvento(evento: String?) {
        this.evento = evento
    }

    fun getDescricao(): String? {
        return descricao
    }

    fun setDescricao(descricao: String?) {
        this.descricao = descricao
    }

    fun getUserID(): String? {
        return UserID
    }

    fun setUserID(usuario: String?) {
        UserID = usuario
    }

    fun getData(): String? {
        return data
    }

    fun setData(data: String?) {
        this.data = data
    }

    fun getStatus(): String? {
        return status
    }

    fun setStatus(status: String?) {
        this.status = status
    }
}