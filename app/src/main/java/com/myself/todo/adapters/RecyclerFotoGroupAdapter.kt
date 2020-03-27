package com.myself.todo.adapters
import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.myself.todo.R
import com.myself.todo.databinding.AlbumGroupLayoutBinding
import com.myself.todo.model.beans.AlbumHead
import java.util.*

class RecyclerFotoGroupAdapter(val activity: Activity, var albumlist: ArrayList<AlbumHead>) : RecyclerView.Adapter<RecyclerFotoGroupAdapter.PicturesGroupViewHolder>() {




    override fun getItemCount(): Int {
       return albumlist.size
    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PicturesGroupViewHolder {
        val albumGroupLayoutBinding:AlbumGroupLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(activity),R.layout.album_group_layout,parent,false)
        return  PicturesGroupViewHolder(albumGroupLayoutBinding)
    }

    override fun onBindViewHolder(holder: PicturesGroupViewHolder, position: Int) {
        val albumHead = albumlist[position]
        val albumGroupLayoutBinding = holder.albumGroupLayoutBinding
        albumGroupLayoutBinding.albumhead = albumHead
        val fotosadapter = RecyclerFotoAdapter(activity,albumHead.pictures)
        val gridLayoutManager = GridLayoutManager(activity,2,RecyclerView.VERTICAL ,false)
        albumGroupLayoutBinding.picturesrecycler.adapter = fotosadapter
        albumGroupLayoutBinding.picturesrecycler.layoutManager = gridLayoutManager
    }

    class PicturesGroupViewHolder(val albumGroupLayoutBinding: AlbumGroupLayoutBinding) : RecyclerView.ViewHolder(albumGroupLayoutBinding.root)

}