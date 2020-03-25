package com.myself.todo.adapters

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.media.MediaPlayer.OnPreparedListener
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.mmin18.widget.RealtimeBlurView
import com.myself.todo.Beans.Music
import com.myself.todo.Database.AlbumRepository
import com.myself.todo.Database.MusicRepository
import com.myself.todo.R
import org.junit.runner.RunWith
import java.io.IOException

class RecyclerMusicAdapter(private val mContext: Context?, private val mActivity: Activity?, private val mData: MutableList<Music?>?) : RecyclerView.Adapter<RecyclerMusicAdapter.MyViewHolder?>() {
    var i = 0
    var lst: AlbumRepository? = null
    private val myDialog: Dialog? = null
    var mp: MediaPlayer? = MediaPlayer()
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyViewHolder? {
        val view: View
        val mInflater = LayoutInflater.from(mContext)
        view = mInflater.inflate(R.layout.musiccard, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder?, position: Int) {
        Glide.with(mContext).load(mData.get(position).getMusicuri()).into(holder.pic)
        val myanim2 = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_bottom)
        val fade = AnimationUtils.loadAnimation(mContext, R.anim.fade_in)
        val pulse = AnimationUtils.loadAnimation(mContext, R.anim.pulse)
        if (mData.get(position).getStatus() == null) {
            return
        } else {
            if (mData.get(position).getStatus() == "F") {
                holder.fav.setChecked(true)
            }
        }
        holder.card.startAnimation(myanim2)
        holder.pic.startAnimation(fade)
        holder.fav.setOnClickListener(View.OnClickListener {
            if (holder.fav.isChecked()) {
                val music = Music()
                music.musicuri = mData.get(position).getMusicuri()
                music.music = mData.get(position).getMusic()
                val musicRepository = MusicRepository(mContext)
                musicRepository.abrir()
                musicRepository.inserir(music)
            } else {
                val music = Music()
                music.musicuri = mData.get(position).getMusicuri()
                music.music = mData.get(position).getMusic()
                val musicRepository = MusicRepository(mContext)
                musicRepository.abrir()
                musicRepository.unfavoritar(mData.get(position).getMusicuri())
            }
        })
        val path: Uri?
        println(mData.get(position).getMusicuri())
        path = Uri.parse(mData.get(position).getMusicuri())
        val myRetriever = MediaMetadataRetriever()
        myRetriever.setDataSource(mContext, path) // the URI of audio file
        holder.music.setText(myRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE))
        val artwork: ByteArray?
        artwork = myRetriever.embeddedPicture
        if (artwork != null) {
            val bMap = BitmapFactory.decodeByteArray(artwork, 0, artwork.size)
            Glide.with(mContext).load(bMap).into(holder.pic)
        } else {
            Glide.with(mContext).load(R.drawable.album).into(holder.pic)
        }
        holder.play.setOnClickListener(View.OnClickListener {
            if (holder.play.isChecked()) {
                mp = MediaPlayer()
                try {
                    holder.blurView.setBlurRadius(10f)
                    println(mData.get(position).getMusicuri())
                    mp.setDataSource(mData.get(position).getMusicuri())
                    mp.prepareAsync()
                    mp.setOnPreparedListener(OnPreparedListener {
                        mp.start()
                        holder.play.setChecked(true)
                        holder.play.startAnimation(pulse)
                    })
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            } else {
                mp.release()
                holder.play.setChecked(false)
                holder.play.clearAnimation()
            }
        })
    }

    override fun getItemCount(): Int {
        return if (mData.size == 0) {
            0
        } else {
            mData.size
        }
    }

    class MyViewHolder(view: View?) : RecyclerView.ViewHolder(view) {
        var blurView: RealtimeBlurView?
        var music: TextView?
        var pic: ImageView?
        var play: CheckBox?
        var fav: CheckBox?
        var card: CardView?

        init {
            fav = itemView.findViewById<CheckBox?>(R.id.favbtn)
            play = itemView.findViewById<CheckBox?>(R.id.play)
            music = itemView.findViewById<TextView?>(R.id.musicname)
            blurView = itemView.findViewById(R.id.blur)
            pic = itemView.findViewById<ImageView?>(R.id.albumcover)
            card = itemView.findViewById(R.id.musicacard)
        }
    }

}