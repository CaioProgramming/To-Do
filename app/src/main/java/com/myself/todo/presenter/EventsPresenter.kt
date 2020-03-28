package com.myself.todo.presenter
import android.text.Html
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.myself.todo.adapters.RecyclerAdapter
import com.myself.todo.databinding.FragmentEventsBinding
import com.myself.todo.model.EventsDB
import com.myself.todo.model.ModelListeners
import com.myself.todo.model.beans.Events
import com.myself.todo.view.fragments.EventsFragment

class EventsPresenter(val eventsFragment: EventsFragment) : PresenterBase(eventsFragment), ModelListeners.EventosLoadedCompleteListener {
    private var showingfavorites = false
    var recyclerAdapter: RecyclerAdapter? = null
    var eventsDB: EventsDB? = null
    private var eventsBinding: FragmentEventsBinding? = null
    val user = FirebaseAuth.getInstance().currentUser



    fun updaterecycler(eventslist: ArrayList<Events>){
        if (recyclerAdapter != null) {
            recyclerAdapter?.let {
                recyclerAdapter!!.eventList.clear()
                it.eventList.addAll(eventslist)
                it.notifyItemRangeChanged(0,eventslist.size)
            }
        }else{
            recyclerAdapter = RecyclerAdapter(activity,eventslist)
            eventsBinding?.recyclertasks?.let {
                it.adapter = recyclerAdapter
                it.layoutManager = GridLayoutManager(activity,2,RecyclerView.VERTICAL,false)
            }
        }
        val text = user?.let {Html.fromHtml("Olá <b>${user.displayName}<b> \nVocê possui ${recyclerAdapter?.itemCount} tarefas.")}
        eventsBinding?.title?.text = text
    }

    override fun initview() {
        eventsBinding = eventsFragment.eventsBinding
        if (eventsDB == null) {
            eventsDB = EventsDB(activity)
        }
        eventsDB?.setLoadCompleteListener(this)
        eventsDB?.carregar()
        eventsBinding?.favoritesbutton?.setOnClickListener {
            if (!showingfavorites){
                val filteredlist = recyclerAdapter!!.eventList.filter { events -> events.favorite }
                recyclerAdapter!!.eventList = filteredlist as ArrayList<Events>
                recyclerAdapter!!.notifyDataSetChanged()
            }else{
                eventsDB?.carregar()
            }
        }
    }

    override fun loadComplete(eventos: ArrayList<Events>) {
        updaterecycler(eventos)
    }


}