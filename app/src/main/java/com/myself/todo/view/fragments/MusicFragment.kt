package com.myself.todo.view.fragments

import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.myself.todo.adapters.RecyclerMusicAdapter
import com.myself.todo.Beans.Music
import com.myself.todo.Database.MusicRepository
import com.myself.todo.R
import org.junit.runner.RunWith
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class MusicFragment : Fragment() {
    var lstmusic: MutableList<Music?>? = null
    var musicRepository: MusicRepository? = null
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.musicas, container, false)
        lstmusic = ArrayList()
        val recycler: RecyclerView = view.findViewById(R.id.recyclerviewmusicas)
        val empty = view.findViewById<LinearLayout?>(R.id.nomusics)
        loadsongs()
        val songAdapter = RecyclerMusicAdapter(context, activity, lstmusic)
        val llm = GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)
        recycler.setHasFixedSize(true)
        println(lstmusic.size)
        //musicRepository.fecha();
        val myadapter = RecyclerMusicAdapter(context, activity, lstmusic)
        recycler.adapter = myadapter
        recycler.layoutManager = llm
        empty.setVisibility(View.INVISIBLE)
        val myanim2 = AnimationUtils.loadAnimation(context, R.anim.popin)
        recycler.visibility = View.VISIBLE
        recycler.startAnimation(myanim2)
        return view
    }

    private fun loadsongs() {
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val selection = MediaStore.Audio.Media.IS_MUSIC + "!=0"
        val cursor = Objects.requireNonNull(activity).getContentResolver().query(uri, null, selection, null, MediaStore.Audio.Media.DATE_MODIFIED)
        if (cursor != null) {
            if (cursor.moveToLast()) {
                do {
                    val name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME))
                    val artistname = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                    val url = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
                    //String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
                    val music = Music()
                    music.music = name
                    music.musicuri = url
                    music.status = "N"
                    lstmusic.add(music)
                } while (cursor.moveToPrevious())
            }
        }
        assert(cursor != null)
        cursor.close()
    }
}