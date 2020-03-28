package com.myself.todo.model
import android.app.Activity
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.myself.todo.model.beans.Events
import com.myself.todo.model.beans.Tarefas

class EventsDB(activity: Activity) : ModelBase(activity),ValueEventListener {

    init {
        this.path = "Events"
        succesmesage = "Evento salvo com sucesso! \uD83D\uDE0C"
        errormessage = "Ocorreu um erro salvando o seu evento... \uD83D\uDE2D"
        confirmmessage = "Tem certeza que deseja remover os eventos?"
    }


    var eventoLoadedListener = object : ModelListeners.EventosLoadedCompleteListener {
        override fun loadComplete(eventos: ArrayList<Events>) {
            Toast.makeText(activity, "Carregado ${eventos.size} eventos!", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCancelled(p0: DatabaseError) {
        errormessage = p0.message
        taskError()
    }
    override fun onDataChange(dataSnapshot: DataSnapshot) {
        val eventslist = ArrayList<Events>()
        val createevent = Events()
        createevent.id = "createEvnt"
        eventslist.add(createevent)
        for (d in dataSnapshot.children){
            val e: Events? = d.getValue(Events::class.java)
            e?.id = d.key
            e?.let {
                getTarefas(it)
                eventslist.add(it)
            }
        }
        eventoLoadedListener.loadComplete(eventslist)
    }


    fun setLoadCompleteListener(EventosLoadedCompleteListener: ModelListeners.EventosLoadedCompleteListener) {
        this.eventoLoadedListener = EventosLoadedCompleteListener
    }



    fun getTarefas(event: Events){
        event.id?.let {
            raiz.child(it).child("Tarefas").addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                event.tasks = ArrayList()
                for (d in dataSnapshot.children){
                    val t: Tarefas? = d.getValue(Tarefas::class.java)
                    t?.let { it1 -> event.tasks.add(it1) }
                }
            }
        })
        }
    }




    override fun carregar() {
        raiz.orderByChild("userID").equalTo(user!!.uid).addValueEventListener(this)
    }
}