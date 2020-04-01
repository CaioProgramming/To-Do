package com.myself.todo.adapters

import android.app.Activity
import android.text.Html
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.HORIZONTAL
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.myself.todo.R
import com.myself.todo.databinding.AlbumGroupLayoutBinding
import com.myself.todo.model.EventsDB
import com.myself.todo.model.FotosDB
import com.myself.todo.model.ModelListeners
import com.myself.todo.model.beans.Album
import com.myself.todo.model.beans.Events

class ProfileRecyclerAdapter(val activity: Activity) : RecyclerView.Adapter<ProfileRecyclerAdapter.ProfileViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val albumGroupLayoutBinding: AlbumGroupLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.album_group_layout, parent, false)
        return ProfileViewHolder(albumGroupLayoutBinding)
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
        fotosDB.carregar(object : ModelListeners.FotosLoadedCompleteListener {
            override fun loadComplete(pictures: ArrayList<Album>) {
                albumGroupLayoutBinding.title.text = Html.fromHtml("<b>${pictures.size}</b> fotos salvas")
                albumGroupLayoutBinding.picturesrecycler.adapter = RecyclerFotoAdapter(activity,pictures)
                albumGroupLayoutBinding.picturesrecycler.layoutManager = GridLayoutManager(activity,2, VERTICAL,false)
                albumGroupLayoutBinding.nopictures.visibility = if (pictures.size == 0) GONE else VISIBLE
            }
        })
    }

    private fun loadEvents(albumGroupLayoutBinding: AlbumGroupLayoutBinding){
        val eventsDB = EventsDB(activity)
        eventsDB.carregar(object : ModelListeners.EventosLoadedCompleteListener {
            override fun loadComplete(eventos: ArrayList<Events>) {
                albumGroupLayoutBinding.title.text = Html.fromHtml("<b>${eventos.size}</b> Eventos")
                albumGroupLayoutBinding.picturesrecycler.adapter = RecyclerAdapter(activity,eventos)
                albumGroupLayoutBinding.picturesrecycler.layoutManager = LinearLayoutManager(activity, HORIZONTAL,false)
                albumGroupLayoutBinding.nopictures.visibility = if (eventos.size == 0) GONE else VISIBLE
            }
        })
    }

    override fun getItemCount(): Int {
        return 2
    }







    class ProfileViewHolder(val albumGroupLayoutBinding: AlbumGroupLayoutBinding) : RecyclerView.ViewHolder(albumGroupLayoutBinding.root)



}