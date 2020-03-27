package com.myself.todo.model

import com.myself.todo.Beans.Album
import com.myself.todo.Beans.Events

interface ModelListeners {
    interface fotosLoadedCompleteListener {
        fun loadComplete(pictures: ArrayList<Album>)
    }
    interface eventosLoadedCompleteListener {
        fun loadComplete(eventos: ArrayList<Events>)
    }
}