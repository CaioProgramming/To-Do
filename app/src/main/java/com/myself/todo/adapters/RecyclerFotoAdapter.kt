package com.myself.todo.adapters

import android.app.Activity
import android.graphics.drawable.Drawable
import android.view.*
import android.view.View.GONE
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.mikhaellopez.rxanimation.fadeIn
import com.myself.todo.Beans.Album
import com.myself.todo.R
import com.myself.todo.databinding.CardlayoutfotosBinding
import com.myself.todo.view.alerts.FotoAlert
import java.util.*

class RecyclerFotoAdapter(val activity: Activity,val albumlist: ArrayList<Album>) : RecyclerView.Adapter<RecyclerFotoAdapter.PicturesViewHolder>() {

    override fun onBindViewHolder(holder: PicturesViewHolder, position: Int) {
        val album = albumlist[position]
        val cardlayoutfotosBinding = holder.cardlayoutbind
        val repeatanimation = AnimationUtils.loadAnimation(activity,R.anim.fade_in_repeat)
        cardlayoutfotosBinding.mainshimmer.startAnimation(repeatanimation)
        if (album.id != "adPicture") {
            Glide.with(activity).load(album.fotouri).addListener(object : RequestListener<Drawable> {
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                    cardlayoutfotosBinding.albpic.visibility = GONE
                    return false
                }

                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                    cardlayoutfotosBinding.mainshimmer.stopShimmer()
                    cardlayoutfotosBinding.mainshimmer.clearAnimation()
                    cardlayoutfotosBinding.mainshimmer.fadeIn()
                    return false
                }
            }).into(cardlayoutfotosBinding.albpic)
            cardlayoutfotosBinding.albcard.setOnClickListener { FotoAlert(activity,album)}
        }else{
            Glide.with(activity).load(R.drawable.ic_add_black_24dp).addListener(object : RequestListener<Drawable> {
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                    cardlayoutfotosBinding.albpic.visibility = GONE
                    return false
                }

                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                    cardlayoutfotosBinding.mainshimmer.stopShimmer()
                    cardlayoutfotosBinding.mainshimmer.clearAnimation()
                    cardlayoutfotosBinding.mainshimmer.fadeIn()
                    return false
                }
            }).into(cardlayoutfotosBinding.albpic)
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