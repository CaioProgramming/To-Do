package com.myself.todo.Fragments;


import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.myself.todo.Adapters.RecyclerMusicAdapter;
import com.myself.todo.Beans.Music;
import com.myself.todo.Database.MusicRepository;
import com.myself.todo.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class MusicFragment extends Fragment {

    List<Music> lstmusic;

    MusicRepository musicRepository;


    public MusicFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.musicas, container, false);
        lstmusic = new ArrayList<>();

        RecyclerView recycler = view.findViewById(R.id.recyclerviewmusicas);
        LinearLayout empty = view.findViewById(R.id.nomusics);
        loadsongs();
        RecyclerMusicAdapter songAdapter = new RecyclerMusicAdapter(getContext(), getActivity(), lstmusic);
        GridLayoutManager llm = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
        recycler.setHasFixedSize(true);
        System.out.println(lstmusic.size());
        //musicRepository.fecha();

        RecyclerMusicAdapter myadapter = new RecyclerMusicAdapter(getContext(), getActivity(), lstmusic);
        recycler.setAdapter(myadapter);
        recycler.setLayoutManager(llm);
        empty.setVisibility(View.INVISIBLE);
        final Animation myanim2 = AnimationUtils.loadAnimation(getContext(), R.anim.popin);
        recycler.setVisibility(View.VISIBLE);
        recycler.startAnimation(myanim2);
        return view;


    }

    private void loadsongs() {

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!=0";
        Cursor cursor = Objects.requireNonNull(getActivity()).getContentResolver().query(uri, null, selection, null, MediaStore.Audio.Media.DATE_MODIFIED);
        if (cursor != null) {
            if (cursor.moveToLast()) {
                do {
                    String name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                    String artistname = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                    String url = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                    //String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));

                    Music music = new Music();
                    music.setMusic(name);
                    music.setMusicuri(url);
                    music.setStatus("N");
                    lstmusic.add(music);


                } while (cursor.moveToPrevious());
            }
        }
        assert cursor != null;
        cursor.close();

    }


}
