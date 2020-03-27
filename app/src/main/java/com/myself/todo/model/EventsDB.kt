package com.myself.todo.model
import android.app.Activity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.myself.todo.Beans.Events
import com.myself.todo.Beans.Tarefas
import com.myself.todo.presenter.EventsPresenter

class EventsDB(activity: Activity) : ModelBase(activity),ValueEventListener {


    constructor(eventsPresenter: EventsPresenter) : this(eventsPresenter.activity){
        this.eventsPresenter = eventsPresenter
        this.path = "Events"
        succesmesage = "Evento salvo com sucesso! \uD83D\uDE0C"
        errormessage = "Ocorreu um erro salvando o seu evento... \uD83D\uDE2D"
    }


    var eventsPresenter: EventsPresenter? = null
    var eventoLoadedListener = object : ModelListeners.eventosLoadedCompleteListener {
        override fun loadComplete(eventos: ArrayList<Events>) {
            eventsPresenter?.updaterecycler(eventos)
        }
    }

    override fun onCancelled(p0: DatabaseError) {
        errormessage = p0.message
        taskError()
    }
    override fun onDataChange(dataSnapshot: DataSnapshot) {
        val eventslist = ArrayList<Events>()
        for (d in dataSnapshot.children){
            val e: Events? = d.getValue(Events::class.java)
            e?.id = d.key
            e?.let { getTarefas(it) }
        }
        eventoLoadedListener.loadComplete(eventslist)
    }


    public fun setLoadCompleteListener(eventosLoadedCompleteListener: ModelListeners.eventosLoadedCompleteListener){
        this.eventoLoadedListener = eventosLoadedCompleteListener
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