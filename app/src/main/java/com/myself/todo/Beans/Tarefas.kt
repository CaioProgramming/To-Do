package com.myself.todo.Beans

class Tarefas {

    constructor()
    constructor(tarefa: String?, data: String?, completed: Boolean) {
        this.tarefa = tarefa
        this.data = data
        this.completed = completed
    }

    var tarefa:String? = null
    var data: String? = null
    var completed = false
}