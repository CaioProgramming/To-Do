package com.myself.todo.presenter

import android.app.Activity
import android.content.res.ColorStateList
import android.graphics.Color
import android.text.Html
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.myself.todo.R
import com.myself.todo.adapters.RecyclerAdapter
import com.myself.todo.databinding.FragmentEventsBinding
import com.myself.todo.model.EventsDB
import com.myself.todo.model.ModelListeners
import com.myself.todo.model.beans.Events

class EventsPresenter(activity: Activity, val eventsBinding: FragmentEventsBinding) : PresenterBase(activity, eventsBinding), ModelListeners.EventosLoadedCompleteListener {
    private var showingfavorites = false
    private var recyclerAdapter: RecyclerAdapter? = null
    private var eventsDB: EventsDB? = null


    private fun updaterecycler(eventslist: ArrayList<Events>) {
        if (recyclerAdapter != null) {
                recyclerAdapter!!.eventList.clear()
            recyclerAdapter!!.eventList = eventslist
            recyclerAdapter!!.notifyItemRangeChanged(0, eventslist.size)
        }else{
            recyclerAdapter = RecyclerAdapter(activity,eventslist)
            val gridlayout = GridLayoutManager(activity, 2, VERTICAL, false)
            eventsBinding.recyclertasks.adapter = recyclerAdapter
            eventsBinding.recyclertasks.layoutManager = gridlayout
            eventsBinding.recyclertasks.setHasFixedSize(true)
        }
        val count = eventslist.size - 1
        val text = Html.fromHtml("Olá <b>${user?.displayName}<b> \nVocê possui $count tarefas.")
        eventsBinding.title.text = text
    }

    override fun initview() {
        eventsDB = EventsDB(activity)
        eventsDB!!.setLoadCompleteListener(this)
        eventsDB!!.carregar()
        eventsBinding.favoritesbutton.setOnClickListener {
            if (!showingfavorites){
                val filteredlist = recyclerAdapter!!.eventList.filter { events -> events.favorite }
                recyclerAdapter!!.eventList = filteredlist as ArrayList<Events>
                recyclerAdapter!!.notifyDataSetChanged()
                showingfavorites = true
                changeButton()
            }else{
                eventsDB?.carregar()
                changeButton()
            }

        }
    }

    fun changeButton() {
        val backcolor = if (showingfavorites) {
            Color.WHITE
        } else {
            activity.resources.getColor(R.color.red_500)
        }
        val iconcolor = if (!showingfavorites) {
            Color.WHITE
        } else {
            activity.resources.getColor(R.color.red_500)
        }
        eventsBinding.favoritesbutton.setBackgroundColor(backcolor)
        eventsBinding.favoritesbutton.imageTintList = ColorStateList.valueOf(iconcolor)
    }

    override fun loadComplete(eventos: ArrayList<Events>) {
        updaterecycler(eventos)
    }


}