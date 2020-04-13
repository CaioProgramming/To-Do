package com.myself.todo.model

import com.myself.todo.model.beans.Album
import com.myself.todo.model.beans.Events

interface ModelListeners {
    interface FotosLoadedCompleteListener {
        fun loadComplete(pictures: ArrayList<Album>)
    }

    interface EventosLoadedCompleteListener {
        fun loadComplete(eventos: ArrayList<Events>)
    }

    interface TaskListener {
        fun taskSucess()
        fun taskError()
    }


}