package com.myself.todo.model
import android.app.Activity
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.myself.todo.model.beans.Album

class FotosDB(activity: Activity) : ModelBase(activity),ValueEventListener{
    init {
        path = "Fotos"
        succesmesage = "Foto salva com sucesso! \uD83E\uDD29"
        errormessage = "Ocorreu um erro ao salvar sua foto \uD83E\uDD7A"

    }

    override fun carregar() {
        raiz.addValueEventListener(this)
    }


    var fotosLoadedCompleteListener = object : ModelListeners.FotosLoadedCompleteListener {
        override fun loadComplete(pictures: ArrayList<Album>) {
            Toast.makeText(activity, "Carregou ${pictures.size} fotos", Toast.LENGTH_LONG).show()
        }
    }

    fun setOnFotosLoadedListener(fotoloadedListener: ModelListeners.FotosLoadedCompleteListener) {
        this.fotosLoadedCompleteListener = fotoloadedListener
    }


    override fun onCancelled(p0: DatabaseError) {
        TODO("Not yet implemented")
    }

    override fun onDataChange(dataSnapshot: DataSnapshot) {
        val fotos: ArrayList<Album> = ArrayList()
        val album = Album()
        album.id = "newPicture"
        fotos.add(album)
        for (d in dataSnapshot.children){
            val a: Album? = d.getValue(Album::class.java)
            a?.let { fotos.add(it) }
        }
        fotosLoadedCompleteListener.loadComplete(fotos)
    }




}