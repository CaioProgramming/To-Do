package com.myself.todo.presenter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.myself.todo.Beans.Album
import com.myself.todo.Beans.AlbumHead
import com.myself.todo.adapters.RecyclerFotoGroupAdapter
import com.myself.todo.model.FotosDB
import com.myself.todo.view.fragments.FotosFragment

class FotosPresenter(fragment: FotosFragment) : PresenterBase(fragment) {
    private val fotosBinding = fragment.fotosBinding!!
    private val albumheads: ArrayList<AlbumHead> = ArrayList()
    private var fotoGroupAdapter: RecyclerFotoGroupAdapter? = RecyclerFotoGroupAdapter(fragment.activity!!,albumheads)
    private val fotosDB = FotosDB(this)
    override fun initview() {
        fotoGroupAdapter = RecyclerFotoGroupAdapter(activity,albumheads)
        fotosBinding.recyclerFotos.adapter = fotoGroupAdapter
        fotosBinding.recyclerFotos.layoutManager = LinearLayoutManager(activity,VERTICAL,false)
        carregar()
    }



    fun carregar(){
       fotosDB.carregar()
    }

    fun filterlists(albumlist: ArrayList<Album>){
        val albumfavorites = AlbumHead("Favoritos", albumlist.filter { album -> album.favorite } as ArrayList<Album>)
        val albumpics = AlbumHead("Suas fotos",albumlist)
        val albumHeads = ArrayList<AlbumHead>()
        albumHeads.add(albumfavorites)
        albumHeads.add(albumpics)
        fotoGroupAdapter?.albumlist?.clear()
        fotoGroupAdapter?.albumlist?.addAll(albumHeads)
        fotoGroupAdapter?.notifyItemRangeChanged(0,albumheads.size)
    }


}