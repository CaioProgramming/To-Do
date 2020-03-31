package com.myself.todo.model.beans


class Album {
    private var userID: String? = null
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

    fun isAcreatePicture(): Boolean {
        return id.equals("newPicture")
    }

    constructor()


    companion object {
        fun createAddPic(): Album {
            val a = Album()
            a.id = "newPicture"
            a.dia = "Adicionar nova foto"
            return a
        }
    }


}