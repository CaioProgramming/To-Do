package com.myself.todo.model

import android.app.Activity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.myself.todo.model.beans.Events
import com.myself.todo.model.beans.Tarefas
import de.mateware.snacky.Snacky

class TasksDB(activity: Activity, val event: Events) : ModelBase(activity), ValueEventListener {
    override var path = "Events/${event.id}/tasks"
    override var raiz = FirebaseDatabase.getInstance().reference.child(user!!.uid).child(path)

    init {
        succesmesage = "Tarefa salva com sucesso! \uD83E\uDD29"
        errormessage = "Ocorreu um erro ao salvar sua tarefa \uD83E\uDD7A"
    }

    override fun onCancelled(p0: DatabaseError) {
        taskError()
    }

    var tasksLoadedListener = object : ModelListeners.TasksLoadedListener {
        override fun loadComplete(tarefas: ArrayList<Tarefas>) {
            Snacky.builder().setActivity(activity).success().setText("Carregou ${tarefas.size}").show()
        }
    }

    fun setTaskLoadedListener(tasksLoadedListener: ModelListeners.TasksLoadedListener) {
        this.tasksLoadedListener = tasksLoadedListener
    }


    fun carregar(tasksLoadedListener: ModelListeners.TasksLoadedListener) {
        setTaskLoadedListener(tasksLoadedListener)
        raiz.addValueEventListener(this)
    }

    override fun onDataChange(dataSnapshot: DataSnapshot) {
        val tarefas = ArrayList<Tarefas>()
        for (d in dataSnapshot.children) {
            val t: Tarefas? = d.getValue(Tarefas::class.java)
            t?.let { tarefas.add(it) }
        }
        tasksLoadedListener.loadComplete(tarefas)
    }


}