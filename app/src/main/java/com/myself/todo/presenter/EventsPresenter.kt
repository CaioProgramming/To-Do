package com.myself.todo.presenter
import android.app.Activity
import android.text.Html
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.myself.todo.adapters.RecyclerAdapter
import com.myself.todo.Beans.Events
import com.myself.todo.view.fragments.EventsFragment
import com.myself.todo.model.EventsDB

class EventsPresenter(eventsFragment: EventsFragment): PresenterBase(eventsFragment) {
    private var showingfavorites = false
    var recyclerAdapter: RecyclerAdapter? = null
    private val eventsDB = EventsDB(this)
    val activity: Activity = eventsFragment.activity!!
    private val eventsBinding = eventsFragment.eventsBinding!!
    val user = FirebaseAuth.getInstance().currentUser



    fun updaterecycler(eventslist: ArrayList<Events>){
        if (recyclerAdapter != null) {
            recyclerAdapter?.let {
                it.eventList.addAll(eventslist)
                it.notifyItemRangeChanged(0,eventslist.size)
            }
        }else{
            recyclerAdapter = RecyclerAdapter(activity,eventslist)
            eventsBinding.recyclertasks.let {
                it.adapter = recyclerAdapter
                it.layoutManager = GridLayoutManager(activity,2,RecyclerView.VERTICAL,false)
            }
        }
        val text = user?.let {Html.fromHtml("Olá <b>${user.displayName}<b> \nVocê possui ${recyclerAdapter?.itemCount} tarefas.")}
        eventsBinding.title.text = text
    }

    override fun initview() {
        eventsDB.carregar()
        eventsBinding.favoritesbutton.setOnClickListener {
            if (!showingfavorites){
                val filteredlist = recyclerAdapter!!.eventList.filter { events -> events.favorite }
                recyclerAdapter!!.eventList = filteredlist as ArrayList<Events>
                recyclerAdapter!!.notifyDataSetChanged()
            }else{
                eventsDB.carregar()
            }
        }
    }




}