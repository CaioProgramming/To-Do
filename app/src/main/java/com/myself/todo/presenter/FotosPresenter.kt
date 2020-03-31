package com.myself.todo.presenter

import android.app.Activity
import android.text.Html
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.myself.todo.adapters.RecyclerFotoAdapter
import com.myself.todo.adapters.RecyclerFotoGroupAdapter
import com.myself.todo.databinding.FragmentFotosBinding
import com.myself.todo.model.FotosDB
import com.myself.todo.model.ModelListeners
import com.myself.todo.model.beans.Album
import com.myself.todo.model.beans.AlbumHead

class FotosPresenter(activity: Activity, private val fotosBinding: FragmentFotosBinding) : PresenterBase(activity, fotosBinding), ModelListeners.FotosLoadedCompleteListener {
    private val albumheads: ArrayList<AlbumHead> = ArrayList()
    private var fotoGroupAdapter: RecyclerFotoGroupAdapter? = null
    private var fotosDB: FotosDB? = null
    override fun initview() {
        fotosDB = FotosDB(activity)
        fotosDB!!.carregar(this)
    }



    private fun filterlists(albumlist: ArrayList<Album>) {
        val text = user?.let { Html.fromHtml("Olá <b>${user.displayName}</b>, \nVocê possui ${albumlist.size} fotos salvas.") }
        fotosBinding.title.text = text
        val album = Album.createAddPic()
        albumlist.add(album)
        val albumfavorites = AlbumHead("Seus favoritos", albumlist.filter { album -> album.favorite } as ArrayList<Album>)
        val albumpics = AlbumHead("Suas fotos",albumlist)
        val albumHeads = ArrayList<AlbumHead>()
        albumHeads.add(albumfavorites)
        albumHeads.add(albumpics)
        var fotoGroupAdapter = RecyclerFotoAdapter(activity, albumlist)
        val layoutManager = LinearLayoutManager(activity, VERTICAL, false)
        fotosBinding.recyclerFotos.adapter = fotoGroupAdapter
        fotosBinding.recyclerFotos.layoutManager = layoutManager

    }

    override fun loadComplete(pictures: ArrayList<Album>) {
        filterlists(pictures)
    }


}