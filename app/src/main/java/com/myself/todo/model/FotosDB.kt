package com.myself.todo.model
import android.app.Activity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.myself.todo.Beans.Album
import com.myself.todo.presenter.FotosPresenter

class FotosDB(activity: Activity) : ModelBase(activity),ValueEventListener {
    init {
        path = "Fotos"
    }
    var fotosPresenter: FotosPresenter? = null

    constructor(fotosPresenter: FotosPresenter) : this(fotosPresenter.activity){
        this.fotosPresenter = fotosPresenter
    }

    override fun carregar() {
        raiz.addValueEventListener(this)
    }

    override fun onCancelled(p0: DatabaseError) {
        TODO("Not yet implemented")
    }

    override fun onDataChange(dataSnapshot: DataSnapshot) {
        val fotos: ArrayList<Album> = ArrayList()
        fotos.clear()
        for (d in dataSnapshot.children){
            val a: Album? = d.getValue(Album::class.java)
            a?.let { fotos.add(it) }
        }
        fotosPresenter?.filterlists(fotos)
    }


}