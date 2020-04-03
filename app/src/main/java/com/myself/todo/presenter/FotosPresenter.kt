package com.myself.todo.presenter

import android.app.Activity
import android.text.Html
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.myself.todo.adapters.RecyclerFotoAdapter
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
        fotoAdapter = RecyclerFotoAdapter(activity, albumlist)
        val layoutManager = GridLayoutManager(activity, 2, VERTICAL, false)
        val onSpanSizeLookup: SpanSizeLookup = object : SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (position) {
                    0, 1, 2, fotoAdapter?.itemCount -> 1
                    else -> 2
                }
            }
        }
        layoutManager.spanSizeLookup = onSpanSizeLookup
        fotosBinding.recyclerFotos.adapter = fotoAdapter
        fotosBinding.recyclerFotos.layoutManager = layoutManager
    }

    override fun loadComplete(pictures: ArrayList<Album>) {
        filterlists(pictures)
    }


}