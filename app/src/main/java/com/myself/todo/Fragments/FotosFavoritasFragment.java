package com.myself.todo.Fragments;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.myself.todo.Adapters.RecyclerFotoFavoritaAdapter;
import com.myself.todo.Beans.Album;
import com.myself.todo.Database.AlbumRepository;
import com.myself.todo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FotosFavoritasFragment extends Fragment {

    List<Album> lstalbum;
    AlbumRepository albRepository;


    public FotosFavoritasFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fotos, container, false);
        FrameLayout frameLayout = view.findViewById(R.id.fotos);
        lstalbum = new ArrayList<>();

        RecyclerView recycler = view.findViewById(R.id.recyclerviewfotos);
        LinearLayout empty = view.findViewById(R.id.nofotos);
        albRepository = new AlbumRepository(getActivity());
        albRepository.abrir();
        Intent intent = getActivity().getIntent();

        String usuario = intent.getExtras().getString("usuario");
        Cursor evento = albRepository.obterFavoritos(usuario);
        System.out.println(evento.getCount());
        evento.moveToFirst();

        if (evento.getCount() == 0) {

        } else {

            empty.setVisibility(View.INVISIBLE);
            recycler.setVisibility(View.VISIBLE);


        }
        for (int i = 0; i < evento.getCount(); i++) {

            lstalbum.add(albRepository.criafoto(evento));
            evento.moveToNext();

        }
        GridLayoutManager llm = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
        recycler.setHasFixedSize(true);
        System.out.println(lstalbum.size());
        albRepository.fecha();

        RecyclerFotoFavoritaAdapter myadapter = new RecyclerFotoFavoritaAdapter(getContext(), getActivity(), lstalbum);
        recycler.setAdapter(myadapter);
        recycler.setLayoutManager(llm);
        final Animation myanim2 = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_bottom);
        recycler.startAnimation(myanim2);
        return view;


    }


}
