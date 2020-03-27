package com.myself.todo.Beans


class Album {
    var userID: String? = null
    var fotouri: String? = null
    var description: String? = null
    var dia: String? = null
   var favorite = false
    var id: String? = null

    constructor(id: String?, fotouri: String?, description: String?, dia: String?, favorite: Boolean, userID: String?) {
        this.fotouri = fotouri
        this.description = description
        this.dia = dia
        this.favorite = favorite
        this.id = id
        this.userID = userID
    }

    constructor()


}