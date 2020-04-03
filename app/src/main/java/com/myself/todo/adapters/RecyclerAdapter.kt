package com.myself.todo.adapters

import android.app.Activity
import android.app.AlertDialog
import android.os.Handler
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mikhaellopez.rxanimation.fadeIn
import com.myself.todo.R
import com.myself.todo.databinding.EventCardHorizontalLayoutBinding
import com.myself.todo.databinding.EventCardLayoutBinding
import com.myself.todo.model.EventsDB
import com.myself.todo.model.beans.Events
import com.myself.todo.view.alerts.NewEventAlert

class RecyclerAdapter(val activity: Activity, var eventList: ArrayList<Events>?, val horizontal: Boolean) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (!horizontal) {
            val cardlayoutBinding: EventCardLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.event_card_layout, parent, false)
            return EventsViewHolder(cardlayoutBinding)
        } else {
            val cardlayoutBinding: EventCardHorizontalLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.event_card_layout_horizontal, parent, false)
            return EventsHorizontalViewHolder(cardlayoutBinding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (horizontal) configurehorizontal(holder, position) else configurevertical(holder, position)
    }


    private fun configurevertical(holder: RecyclerView.ViewHolder, position: Int) {
        val verticalholder = holder as EventsViewHolder
        if (eventList != null && eventList!!.size > 0) {
            val event = eventList!![position]
            verticalholder.cardlayoutBinding.event = event
            verticalholder.cardlayoutBinding.eventcard.setOnLongClickListener {
                AlertDialog.Builder(activity)
                        .setTitle("Tem certeza")
                        .setMessage("Deseja remover essa atividade?")
                        .setPositiveButton("Remover") { dialog, which -> EventsDB(activity).remover(event.id!!) }
                        .create()
                        .show()
                return@setOnLongClickListener false
            }
            if (event.favorite) {
                holder.cardlayoutBinding.eventcard.isChecked = true
            }
            verticalholder.cardlayoutBinding.eventcard.setOnClickListener {
                addnewevent()
            }
            val handler = Handler()
            handler.postDelayed({ holder.cardlayoutBinding.cardShimmer.hideShimmer() }, 3000)
        } else {
            val repeat = AnimationUtils.loadAnimation(activity, R.anim.fade_in_repeat)
            verticalholder.cardlayoutBinding.cardShimmer.startAnimation(repeat)
        }
        verticalholder.cardlayoutBinding.cardShimmer.fadeIn()

    }

    private fun configurehorizontal(holder: RecyclerView.ViewHolder, position: Int) {
        val verticalholder = holder as EventsHorizontalViewHolder
        if (eventList != null && eventList!!.size > 0) {
            val event = eventList!![position]
            verticalholder.cardlayoutBinding.event = event
            verticalholder.cardlayoutBinding.eventcard.setOnLongClickListener {
                AlertDialog.Builder(activity)
                        .setTitle("Tem certeza")
                        .setMessage("Deseja remover essa atividade?")
                        .setPositiveButton("Remover") { dialog, which -> EventsDB(activity).remover(event.id!!) }
                        .create()
                        .show()
                return@setOnLongClickListener false
            }
            if (event.favorite) {
                holder.cardlayoutBinding.eventcard.isChecked = true
            }
            verticalholder.cardlayoutBinding.eventcard.setOnClickListener {
                addnewevent()
            }
            val handler = Handler()
            handler.postDelayed({ holder.cardlayoutBinding.cardShimmer.hideShimmer() }, 3000)
        } else {
            val repeat = AnimationUtils.loadAnimation(activity, R.anim.fade_in_repeat)
            verticalholder.cardlayoutBinding.cardShimmer.startAnimation(repeat)
        }
        verticalholder.cardlayoutBinding.cardShimmer.fadeIn()

    }


    private fun addnewevent() {
        val newevent = NewEventAlert(activity)
        newevent.setupAlert()
    }


    override fun getItemCount(): Int {
        return if (eventList != null) eventList!!.size else 4
    }

    class EventsViewHolder(val cardlayoutBinding: EventCardLayoutBinding) : RecyclerView.ViewHolder(cardlayoutBinding.root)
    class EventsHorizontalViewHolder(val cardlayoutBinding: EventCardHorizontalLayoutBinding) : RecyclerView.ViewHolder(cardlayoutBinding.root)




}