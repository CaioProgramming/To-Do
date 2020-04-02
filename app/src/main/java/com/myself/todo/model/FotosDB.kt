package com.myself.todo.model
import android.app.Activity
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.myself.todo.Utils.Utilities
import com.myself.todo.model.beans.Album

class FotosDB(activity: Activity) : ModelBase(activity),ValueEventListener{
    init {
        path = "Fotos"
        succesmesage = "Foto salva com sucesso! "
        errormessage = "Ocorreu um erro ao salvar sua foto ${Utilities.randomsadmoji()}"
    }

    fun carregar(fotoloadedListener: ModelListeners.FotosLoadedCompleteListener) {
        setOnFotosLoadedListener(fotoloadedListener)
        raiz.addValueEventListener(this)
    }


    var fotosLoadedCompleteListener = object : ModelListeners.FotosLoadedCompleteListener {
        override fun loadComplete(pictures: ArrayList<Album>) {
            Toast.makeText(activity, "Carregou ${pictures.size} fotos", Toast.LENGTH_LONG).show()
        }
    }

    fun setOnFotosLoadedListener(fotoloadedListener: ModelListeners.FotosLoadedCompleteListener) {
        fotosLoadedCompleteListener = fotoloadedListener
    }


    override fun onCancelled(p0: DatabaseError) {
        taskError()
    }

    override fun onDataChange(dataSnapshot: DataSnapshot) {
        val fotos: ArrayList<Album> = ArrayList()
        for (d in dataSnapshot.children){
            val a: Album? = d.getValue(Album::class.java)

            a?.let {
                it.id = d.key
                fotos.add(it)
            }
        }
        fotosLoadedCompleteListener.loadComplete(fotos)
    }




}