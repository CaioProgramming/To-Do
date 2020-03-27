package com.myself.todo.adapters

import android.app.Activity
import android.view.*
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.myself.todo.Beans.Album
import com.myself.todo.R
import com.myself.todo.databinding.CardlayoutfotosBinding
import com.myself.todo.view.alerts.FotoAlert
import java.util.*

class RecyclerFotoAdapter(val activity: Activity,val albumlist: ArrayList<Album>) : RecyclerView.Adapter<RecyclerFotoAdapter.PicturesViewHolder>() {

    override fun onBindViewHolder(holder: PicturesViewHolder, position: Int) {
        val album = albumlist[position]
        val cardlayoutfotosBinding = holder.cardlayoutbind
        if (album.id != "adPicture") {
            Glide.with(activity).load(album.fotouri).into(cardlayoutfotosBinding.albpic)
            val slideintop = AnimationUtils.loadAnimation(activity,R.anim.slide_in_bottom)
            cardlayoutfotosBinding.albpic.startAnimation(slideintop)
            cardlayoutfotosBinding.albcard.setOnClickListener { FotoAlert(activity,album)}
        }else{
            Glide.with(activity).load(R.drawable.ic_add_black_24dp).into(cardlayoutfotosBinding.albpic)
            cardlayoutfotosBinding.albcard.setOnClickListener { addNewPic()}

        }

    }

    private fun addNewPic(){}


    override fun getItemCount(): Int {
       return albumlist.size
    }

    class PicturesViewHolder(val cardlayoutbind: CardlayoutfotosBinding) : RecyclerView.ViewHolder(cardlayoutbind.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PicturesViewHolder {
        val cardlayoutfotosBinding:CardlayoutfotosBinding = DataBindingUtil.inflate(LayoutInflater.from(activity),R.layout.cardlayoutfotos,parent,false)
        return  PicturesViewHolder(cardlayoutfotosBinding)
    }


}