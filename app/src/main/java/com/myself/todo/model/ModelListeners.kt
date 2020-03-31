package com.myself.todo.model

import com.myself.todo.model.beans.Album
import com.myself.todo.model.beans.Events
import com.myself.todo.model.beans.Tarefas

interface ModelListeners {
    interface FotosLoadedCompleteListener {
        fun loadComplete(pictures: ArrayList<Album>)
    }

    interface EventosLoadedCompleteListener {
        fun loadComplete(eventos: ArrayList<Events>)
    }

    interface TasksLoadedListener {
        fun loadComplete(tarefas: ArrayList<Tarefas>)
    }
}