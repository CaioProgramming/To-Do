package com.myself.todo.model

import android.app.Activity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.myself.todo.Beans.Events
import com.myself.todo.Beans.Tarefas
import com.myself.todo.presenter.EventsPresenter

class EventsDB(activity: Activity) : EventBase(activity),ValueEventListener {


    constructor(eventsPresenter: EventsPresenter) : this(eventsPresenter.activity){
        this.eventsPresenter = eventsPresenter
    }


    var eventsPresenter: EventsPresenter? = null


    override fun onCancelled(p0: DatabaseError) {
        TODO("Not yet implemented")
    }
    override fun onDataChange(dataSnapshot: DataSnapshot) {
        val eventslist = ArrayList<Events>()
        for (d in dataSnapshot.children){
            val e: Events? = d.getValue(Events::class.java)
            e?.id = d.key
            e?.let { getTarefas(it) }
        }
        eventsPresenter?.updaterecycler(eventslist)
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
                    t?.let { it1 -> event.tasks!!.add(it1) }
                }
            }
        })
        }
    }



    override fun carregar() {
        raiz.orderByChild("userID").equalTo(user!!.uid).addValueEventListener(this)
    }
}