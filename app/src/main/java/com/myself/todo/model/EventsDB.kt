package com.myself.todo.model
import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.myself.todo.Utils.Utilities.Companion.randomhappymoji
import com.myself.todo.Utils.Utilities.Companion.randomsadmoji
import com.myself.todo.model.beans.Events

class EventsDB(activity: Activity) : ModelBase(activity),ValueEventListener {
    override var path = "Events"
    override var reference = raiz.child(path)
    init {
        succesmesage = "Evento salvo com sucesso! ${randomhappymoji()}"
        errormessage = "Ocorreu um erro salvando o seu evento... ${randomsadmoji()}"
        confirmmessage = "Tem certeza que deseja remover seus eventos?"
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

        for (d in dataSnapshot.children){
            val e: Events? = d.getValue(Events::class.java)
            e?.id = d.key
            e?.let {
                eventslist.add(it)
            }
        }
        Log.i(javaClass.simpleName, "Carregou ${eventslist.size}")
        print(eventslist)
        eventoLoadedListener.loadComplete(eventslist)
    }


    fun setLoadCompleteListener(EventosLoadedCompleteListener: ModelListeners.EventosLoadedCompleteListener) {
        this.eventoLoadedListener = EventosLoadedCompleteListener
    }





    fun carregar(eventosLoadedCompleteListener: ModelListeners.EventosLoadedCompleteListener) {
        setLoadCompleteListener(eventosLoadedCompleteListener)
        reference.addValueEventListener(this)
    }
}