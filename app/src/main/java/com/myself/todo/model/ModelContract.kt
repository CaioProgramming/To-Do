package com.myself.todo.model

interface ModelContract{
    fun inserir(obj: Any)
    fun remover(id:String)
    fun alterar(id:String, obj: Any)
}