package com.myself.todo.Beans


class Album {
    private var userID: String? = null
    private var fotouri: String? = null
    private var description: String? = null
    private var dia: String? = null
    private var status: String? = null
    private var id: String? = null

    constructor(id: String?, fotouri: String?, description: String?, dia: String?, status: String?, userID: String?) {
        this.fotouri = fotouri
        this.description = description
        this.dia = dia
        this.status = status
        this.id = id
        this.userID = userID
    }

    constructor()

    fun getUserID(): String? {
        return userID
    }

    fun setUserID(userID: String?) {
        this.userID = userID
    }

    fun getFotouri(): String? {
        return fotouri
    }

    fun setFotouri(fotouri: String?) {
        this.fotouri = fotouri
    }

    fun getDescription(): String? {
        return description
    }

    fun setDescription(description: String?) {
        this.description = description
    }

    fun getDia(): String? {
        return dia
    }

    fun setDia(dia: String?) {
        this.dia = dia
    }

    fun getId(): String? {
        return id
    }

    fun setId(id: String?) {
        this.id = id
    }

    fun getStatus(): String? {
        return status
    }

    fun setStatus(status: String?) {
        this.status = status
    }
}