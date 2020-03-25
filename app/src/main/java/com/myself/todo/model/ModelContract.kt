package com.myself.todo.model

import java.util.*

interface ModelContract{
    fun inserir(obj: Any)
    fun remover(id:String)
    fun alterar(id:String, obj: Any)
    fun carregar()
}