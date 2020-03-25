package com.myself.todo.view.fragments

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.myself.todo.adapters.RecyclerAdapter
import com.myself.todo.Beans.Events
import com.myself.todo.R
import com.myself.todo.databinding.FragmentEventsBinding
import com.myself.todo.presenter.EventsPresenter
import de.mateware.snacky.Snacky
import org.junit.runner.RunWith
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class EventsFragment : Fragment() {
    var eventsBinding: FragmentEventsBinding? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        eventsBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_events,container,false)
        val eventsPresenter = EventsPresenter(this)
        return eventsBinding?.root

    }

    private fun SalvarEvento() {
        val event = neweventtext.getText().toString()
        val desc = neweventdescription.getText().toString()
        val datenow = Calendar.getInstance().time
        val dia = datenow.toString()
        val user = FirebaseAuth.getInstance().currentUser
        if (!TextUtils.isEmpty(event) && !TextUtils.isEmpty(desc)) {
            val id = raiz.push().key
            var e: Events? = null
            if (user != null) {
                e = Events(id, neweventtext.getText().toString(), neweventdescription.getText().toString(), user.uid, dia, "N")
            }
            if (id != null) {
                raiz.child(id).setValue(e).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Snacky.builder().setActivity(Objects.requireNonNull(activity)).success().setText("Evento adicionado com sucesso").show()
                        HideEventform()
                    } else {
                        Snacky.builder().setActivity(Objects.requireNonNull(activity)).error().setText("Erro " + task.exception).show()
                    }
                }
            }
        }
        Carregar(recyclerviewevents, noevents)
    }

    private fun Carregar(recycler: RecyclerView?, empty: LinearLayout?) {
        lstevents.clear()
        recycler.clearOnChildAttachStateChangeListeners()
        val user = FirebaseAuth.getInstance().currentUser
        val raiz = FirebaseDatabase.getInstance().reference.child("events")
        raiz.keepSynced(true)
        if (user != null) {
            raiz.child("events").orderByChild("userID").equalTo(user.uid)
        }
        raiz.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (d in dataSnapshot.children) {
                    val e: Events = d.getValue<Events?>(Events::class.java)
                    val events = Events()
                    if (e != null) {
                        events.evento = e.evento
                        events.descricao = e.descricao
                        events.status = e.status
                        events.data = e.data
                        events.id = e.id
                        events.userID = e.userID
                        println(e.status)
                        lstevents.add(e)
                        println(lstevents.size)
                    }
                }
                recycler.setVisibility(View.VISIBLE)
                val llm = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
                recycler.setHasFixedSize(true)
                println(lstevents.size)
                //final Animation myanim2 = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_bottom);
                val myadapter = RecyclerAdapter(context, activity, lstevents, blur)
                recycler.setAdapter(myadapter)
                recycler.setLayoutManager(llm)
                //recycler.startAnimation(myanim2);
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Snacky.builder().setActivity(Objects.requireNonNull(activity)).error().setText("Erro $databaseError").show()
            }
        })
    }

    private fun CarregarFavoritos(recycler: RecyclerView?, empty: LinearLayout?) {
        lstevents.clear()
        recycler.clearOnChildAttachStateChangeListeners()
        val user = FirebaseAuth.getInstance().currentUser
        val raiz = FirebaseDatabase.getInstance().reference.child("events")
        raiz.keepSynced(true)
        if (user != null) {
            raiz.child("events").orderByChild("userID").equalTo(user.uid)
        }
        raiz.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (d in dataSnapshot.children) {
                    val e: Events = d.getValue<Events?>(Events::class.java)
                    val events = Events()
                    if (e != null) {
                        events.evento = e.evento
                        events.descricao = e.descricao
                        events.status = e.status
                        events.data = e.data
                        events.id = e.id
                        events.userID = e.userID
                        println(e.status)
                        if (events.status == "F") {
                            lstevents.add(e)
                            println(lstevents.size)
                        }
                    }
                }
                recycler.setVisibility(View.VISIBLE)
                val llm = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
                recycler.setHasFixedSize(true)
                println(lstevents.size)
                //final Animation myanim2 = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_bottom);
                val myadapter = RecyclerAdapter(context, activity, lstevents, blur)
                recycler.setAdapter(myadapter)
                recycler.setLayoutManager(llm)
                //recycler.startAnimation(myanim2);
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Snacky.builder().setActivity(Objects.requireNonNull(activity)).error().setText("Erro $databaseError").show()
            }
        })
    }

    private fun CarregarConcluidos(recycler: RecyclerView?, empty: LinearLayout?) {
        lstevents.clear()
        recycler.clearOnChildAttachStateChangeListeners()
        val user = FirebaseAuth.getInstance().currentUser
        val raiz = FirebaseDatabase.getInstance().reference.child("events")
        raiz.keepSynced(true)
        if (user != null) {
            raiz.child("events").orderByChild("userID").equalTo(user.uid)
        }
        raiz.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (d in dataSnapshot.children) {
                    val e: Events = d.getValue<Events?>(Events::class.java)
                    val events = Events()
                    if (e != null) {
                        events.evento = e.evento
                        events.descricao = e.descricao
                        events.status = e.status
                        events.data = e.data
                        events.id = e.id
                        events.userID = e.userID
                        println(e.status)
                        if (events.status == "C") {
                            lstevents.add(e)
                            println(lstevents.size)
                        }
                    }
                }
                recycler.setVisibility(View.VISIBLE)
                val llm = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
                recycler.setHasFixedSize(true)
                println(lstevents.size)
                //final Animation myanim2 = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_bottom);
                val myadapter = RecyclerAdapter(context, activity, lstevents, blur)
                recycler.setAdapter(myadapter)
                recycler.setLayoutManager(llm)
                //recycler.startAnimation(myanim2);
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Snacky.builder().setActivity(Objects.requireNonNull(activity)).error().setText("Erro $databaseError").show()
            }
        })
    }

    private fun ShowEvents() {
        recyclerviewevents.setVisibility(View.VISIBLE)
        val llm = GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)
        recyclerviewevents.setHasFixedSize(true)
        println(lstevents.size)
        if (lstevents.size > 0) {
            noevents.setVisibility(View.INVISIBLE)
            content.setVisibility(View.VISIBLE)
        }
        val myanim2 = AnimationUtils.loadAnimation(context, R.anim.slide_in_bottom)
        val myadapter = RecyclerAdapter(context, activity, lstevents, blur)
        recyclerviewevents.startAnimation(myanim2)
        recyclerviewevents.setAdapter(myadapter)
        recyclerviewevents.setLayoutManager(llm)
    }

    private fun Showform() {
        blur.setVisibility(View.VISIBLE)
        blur.setBlurRadius(50f)
        neweventform.setVisibility(View.VISIBLE)
    }

    private fun HideEventform() {
        blur.setVisibility(View.INVISIBLE)
        neweventform.setVisibility(View.INVISIBLE)
        content.setVisibility(View.VISIBLE)
    }
}