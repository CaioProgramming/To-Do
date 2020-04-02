package com.myself.todo.model

import com.google.firebase.database.DatabaseReference

interface ModelContract{
    var raiz: DatabaseReference
    var path: String
    fun inserir(obj: Any)
    fun remover(id:String)
    fun alterar(id:String, obj: Any)
}