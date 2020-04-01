package com.myself.todo.adapters

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Handler
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import androidx.recyclerview.widget.RecyclerView
import com.myself.todo.R
import com.myself.todo.databinding.CardlayoutBinding
import com.myself.todo.model.EventsDB
import com.myself.todo.model.ModelListeners
import com.myself.todo.model.TasksDB
import com.myself.todo.model.beans.Events
import com.myself.todo.model.beans.Tarefas
import com.myself.todo.view.activities.CreateEventActivity

class RecyclerAdapter(val activity: Activity, var eventList: ArrayList<Events>?) : RecyclerView.Adapter<RecyclerAdapter.EventsViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventsViewHolder {
        val cardlayoutBinding: CardlayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(activity),R.layout.cardlayout,parent,false)
        return EventsViewHolder(cardlayoutBinding)
    }

    override fun onBindViewHolder(holder: EventsViewHolder, position: Int) {
        if (eventList != null && eventList!!.size > 0) {
            val event = eventList!![position]
            holder.cardlayoutBinding.mainlayout.visibility = if (event.isAcreateEvent()){
                GONE
            }else{
                VISIBLE
            }
            holder.cardlayoutBinding.addnewevent.visibility = if (!event.isAcreateEvent()) {
                GONE
            }else{
                VISIBLE
            }
            if (!event.isAcreateEvent()) {
                holder.cardlayoutBinding.titulo.text = event.evento
                holder.cardlayoutBinding.eventcard.setOnLongClickListener {
                    AlertDialog.Builder(activity)
                            .setTitle("Tem certeza")
                            .setMessage("Deseja remover essa atividade?")
                            .setPositiveButton("Remover") { dialog, which -> EventsDB(activity).remover(event.id!!) }
                            .create()
                            .show()
                    return@setOnLongClickListener false
                }
                loadTasks(event, holder.cardlayoutBinding.tasks)
                if (event.favorite){
                    holder.cardlayoutBinding.eventcard.setCardBackgroundColor(activity.resources.getColor(R.color.red_300))
                }

            }else{
                holder.cardlayoutBinding.eventcard.setOnClickListener {
                    addnewevent()
                }
            }

            val handler = Handler()
            handler.postDelayed({ holder.cardlayoutBinding.cardShimmer.hideShimmer() }, 3000)
        }else{
            val repeat = AnimationUtils.loadAnimation(activity,R.anim.fade_in_repeat)
            holder.cardlayoutBinding.cardShimmer.startAnimation(repeat)
        }

    }

    private fun loadTasks(events: Events, recyclerView: RecyclerView) {
        TasksDB(activity, events).carregar(object : ModelListeners.TasksLoadedListener {
            override fun loadComplete(tarefas: ArrayList<Tarefas>) {
                val taskAdapter = RecyclerTarefasAdapter(activity, events, tarefas)
                recyclerView.adapter = taskAdapter
                recyclerView.layoutManager = LinearLayoutManager(activity, VERTICAL, false)
            }
        })

    }

    private fun addnewevent(){
        val i = Intent(activity,CreateEventActivity::class.java)
        activity.startActivity(i)
    }



    override fun getItemCount(): Int {
        if (eventList != null) return eventList!!.size else return 4
    }

    class EventsViewHolder(val cardlayoutBinding: CardlayoutBinding) : RecyclerView.ViewHolder(cardlayoutBinding.root)




}