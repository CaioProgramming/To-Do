package com.myself.todo.presenter

import android.app.Activity
import android.text.Html
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.myself.todo.adapters.RecyclerFotoAdapter
import com.myself.todo.adapters.RecyclerFotoGroupAdapter
import com.myself.todo.databinding.FragmentFotosBinding
import com.myself.todo.model.FotosDB
import com.myself.todo.model.ModelListeners
import com.myself.todo.model.beans.Album
import com.myself.todo.model.beans.AlbumHead

class FotosPresenter(activity: Activity, private val fotosBinding: FragmentFotosBinding) : PresenterBase(activity, fotosBinding), ModelListeners.FotosLoadedCompleteListener {
    private val albumheads: ArrayList<AlbumHead> = ArrayList()
    private var fotoAdapter: RecyclerFotoAdapter? = null
    private var fotosDB: FotosDB? = null
    override fun initview() {
        initializeRecycler()
        fotosDB = FotosDB(activity)
        fotosDB!!.carregar(this)
    }


    fun initializeRecycler(){
        fotoAdapter = RecyclerFotoAdapter(activity,null)
        val layoutManager = GridLayoutManager(activity,2)
        fotoAdapter?.let { fotosBinding.recyclerFotos.adapter = it }
        fotosBinding.recyclerFotos.layoutManager = layoutManager

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
        if (fotoAdapter == null){initializeRecycler()}
        fotoAdapter?.albumlist = albumlist
        fotoAdapter?.notifyDataSetChanged()
    }

    override fun loadComplete(pictures: ArrayList<Album>) {
        filterlists(pictures)
    }


}