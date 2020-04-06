package com.myself.todo.presenter

import android.app.Activity
import android.text.Html
import androidx.annotation.NonNull
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.myself.todo.adapters.RecyclerAdapter
import com.myself.todo.adapters.SwipeToDeleteCallback
import com.myself.todo.databinding.FragmentEventsBinding
import com.myself.todo.model.EventsDB
import com.myself.todo.model.ModelListeners
import com.myself.todo.model.beans.Events


class EventsPresenter(activity: Activity, private val eventsBinding: FragmentEventsBinding) : PresenterBase(activity, eventsBinding), ModelListeners.EventosLoadedCompleteListener {
    private var recyclerAdapter: RecyclerAdapter? = null
    private var eventsDB: EventsDB? = null


    private fun updaterecycler(eventslist: ArrayList<Events>) {
        val text = Html.fromHtml("Olá <b>${user?.displayName}</b> \nVocê possui ${eventslist.size} tarefas.")
        eventsBinding.title.text = text
        eventslist.add(Events.createEvent())
        // Snacky.builder().setActivity(activity).info().setText(text).show()
        if (recyclerAdapter != null) {
            recyclerAdapter!!.eventList = eventslist
            recyclerAdapter!!.notifyDataSetChanged()
            enableSwipeToDeleteAndUndo()

        }else{
            initializeRecycler()
        }

    }

    private fun enableSwipeToDeleteAndUndo() {
        val swipeToDeleteCallback: SwipeToDeleteCallback = object : SwipeToDeleteCallback(activity) {
            override fun onSwiped(@NonNull viewHolder: RecyclerView.ViewHolder, i: Int) {
                val position = viewHolder.adapterPosition
                val item = recyclerAdapter!!.eventList!![position]
                if (!item.isAcreateEvent()) {
                    eventsDB!!.remover(item.id!!)
                }
            }
        }
        val itemTouchhelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchhelper.attachToRecyclerView(eventsBinding.recyclertasks)
    }

    private fun initializeRecycler() {
        recyclerAdapter = RecyclerAdapter(activity, null, false)
        val gridlayout = GridLayoutManager(activity, 1, VERTICAL, false)
        eventsBinding.recyclertasks.adapter = recyclerAdapter
        eventsBinding.recyclertasks.layoutManager = gridlayout

    }

    override fun initview() {
        initializeRecycler()
        eventsDB = EventsDB(activity)
        eventsDB!!.carregar(this)
    }


    override fun loadComplete(eventos: ArrayList<Events>) {
        updaterecycler(eventos)
    }


}