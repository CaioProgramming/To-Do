package com.myself.todo.adapters

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.myself.todo.Beans.Events
import com.myself.todo.R
import com.myself.todo.databinding.CardlayoutBinding

class RecyclerAdapter(val activity: Activity, var eventList: ArrayList<Events?>) : RecyclerView.Adapter<RecyclerAdapter.EventsViewHolder>() {

    init {
        eventList.add(null)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventsViewHolder {

        val cardlayoutBinding: CardlayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(activity),R.layout.cardlayout,parent,false)
        return EventsViewHolder(cardlayoutBinding)
    }

    override fun onBindViewHolder(holder: EventsViewHolder, position: Int) {
        val event = eventList[position]
        holder.cardlayoutBinding.mainlayout.visibility = if (event == null){
            GONE
        }else{
            VISIBLE
        }
        holder.cardlayoutBinding.addnewevent.visibility = if (event != null){
            GONE
        }else{
            VISIBLE
        }
        if (event != null) {
            holder.cardlayoutBinding.titulo.text = event.evento
            holder.cardlayoutBinding.eventcard.setOnLongClickListener {
                val alertDialog = AlertDialog.Builder(activity)
                        .setTitle("Tem certeza")
                        .setMessage("Deseja remover essa atividade?")
                        .create()
                        .show()
                return@setOnLongClickListener false
            }
            event.tasks?.let {
                holder.cardlayoutBinding.tasks.adapter = RecyclerTarefasAdapter(activity,event)
            }
            if (event.favorite){
                holder.cardlayoutBinding.eventcard.setCardBackgroundColor(activity.resources.getColor(R.color.red_500))
            }

        }else{
            holder.cardlayoutBinding.eventcard.setOnClickListener {
                addnewevent()
            }
        }

    }


    private fun addnewevent(){

    }



    override fun getItemCount(): Int {
        return if (eventList.size == 0) {
            1
        } else {
            eventList.size
        }
    }

    class EventsViewHolder(val cardlayoutBinding: CardlayoutBinding) : RecyclerView.ViewHolder(cardlayoutBinding.root)




}