package com.myself.todo.adapters
import android.app.Activity
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.myself.todo.R
import com.myself.todo.databinding.AlbumGroupLayoutBinding
import com.myself.todo.model.beans.AlbumHead
import java.util.*

class RecyclerFotoGroupAdapter(val activity: Activity, var albumlist: ArrayList<AlbumHead>) : RecyclerView.Adapter<RecyclerFotoGroupAdapter.PicturesGroupViewHolder>() {






    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PicturesGroupViewHolder {
        val albumGroupLayoutBinding:AlbumGroupLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(activity),R.layout.album_group_layout,parent,false)
        return  PicturesGroupViewHolder(albumGroupLayoutBinding)
    }

    override fun onBindViewHolder(holder: PicturesGroupViewHolder, position: Int) {
        val albumHead = albumlist[position]
        Log.i("Foto Group", "Loading head ${albumHead.title} with ${albumHead.pictures.size}")
        val albumGroupLayoutBinding = holder.albumGroupLayoutBinding
        albumGroupLayoutBinding.title.text = Html.fromHtml("<b>${albumHead.pictures.size}</b> ${albumHead.title}")
        val fotosadapter = RecyclerFotoAdapter(activity,albumHead.pictures)
        val layoutManager = GridLayoutManager(activity, 2, RecyclerView.VERTICAL, false)
        val onSpanSizeLookup: GridLayoutManager.SpanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (position) {
                    0, 1, 2, albumHead.pictures.lastIndex -> 1
                    else -> 2
                }
            }
        }
        layoutManager.spanSizeLookup = onSpanSizeLookup
        albumGroupLayoutBinding.picturesrecycler.adapter = fotosadapter
        albumGroupLayoutBinding.picturesrecycler.layoutManager = layoutManager
        albumGroupLayoutBinding.nopictures.visibility = if (albumHead.pictures.size == 0) VISIBLE else GONE
    }

    override fun getItemCount(): Int {
        return albumlist.size
    }



    class PicturesGroupViewHolder(val albumGroupLayoutBinding: AlbumGroupLayoutBinding) : RecyclerView.ViewHolder(albumGroupLayoutBinding.root)

}