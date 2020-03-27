package com.myself.todo.adapters

import android.app.Activity
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.HORIZONTAL
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.myself.todo.Beans.Album
import com.myself.todo.Beans.AlbumHead
import com.myself.todo.Beans.Events
import com.myself.todo.databinding.AlbumGroupLayoutBinding
import com.myself.todo.model.EventsDB
import com.myself.todo.model.FotosDB
import com.myself.todo.model.ModelListeners

class ProfileRecyclerAdapter(val activity: Activity) : RecyclerView.Adapter<ProfileRecyclerAdapter.ProfileViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
       val albumGroupLayoutBinding = holder.albumGroupLayoutBinding
        if (position == 0){
            loadEvents(albumGroupLayoutBinding)
       }else{
            loadPictures(albumGroupLayoutBinding)
        }
    }

    private fun loadPictures(albumGroupLayoutBinding: AlbumGroupLayoutBinding){
        val fotosDB = FotosDB(activity)
        fotosDB.setOnFotosLoadedListener(object : ModelListeners.fotosLoadedCompleteListener {
            override fun loadComplete(pictures: ArrayList<Album>) {
                albumGroupLayoutBinding.title.text = "${pictures.size} fotos salvas"
                albumGroupLayoutBinding.picturesrecycler.adapter = RecyclerFotoAdapter(activity,pictures)
                albumGroupLayoutBinding.picturesrecycler.layoutManager = GridLayoutManager(activity,2, VERTICAL,false)
            }
        })
        FotosDB(activity).carregar()
    }

    private fun loadEvents(albumGroupLayoutBinding: AlbumGroupLayoutBinding){
        val eventsDB= EventsDB(activity)
        eventsDB.setLoadCompleteListener(object : ModelListeners.eventosLoadedCompleteListener {
            override fun loadComplete(eventos: ArrayList<Events>) {
                albumGroupLayoutBinding.title.text = "${eventos.size} Eventos"
                albumGroupLayoutBinding.picturesrecycler.adapter = RecyclerAdapter(activity,eventos)
                albumGroupLayoutBinding.picturesrecycler.layoutManager = LinearLayoutManager(activity, HORIZONTAL,false)

            }
        })
    }

    override fun getItemCount(): Int {
        return 2
    }







    class ProfileViewHolder(val albumGroupLayoutBinding: AlbumGroupLayoutBinding) : RecyclerView.ViewHolder(albumGroupLayoutBinding.root)



}