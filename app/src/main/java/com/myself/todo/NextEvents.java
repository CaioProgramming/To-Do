package com.myself.todo;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.myself.todo.Beans.Events;
import com.myself.todo.Database.ListRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class NextEvents extends Fragment {

    List<Events> lstevents;
    ListRepository listRepository;
    public NextEvents() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.favoritevents, container, false);
        FrameLayout frameLayout = view.findViewById(R.id.favoriteevents);
        lstevents = new ArrayList<>();

        RecyclerView recycler = view.findViewById(R.id.recyclerviewfav);
        LinearLayout empty = view.findViewById(R.id.noevents);
        listRepository = new ListRepository(getActivity());
        listRepository.abrir();
        Cursor evento = listRepository.obterFavoritos();
        evento.moveToFirst();

        if (evento.getCount() == 0) {

        }else {

            empty.setVisibility(View.INVISIBLE);
            recycler.setVisibility(View.VISIBLE);

        }
        for (int i = 0 ;i < evento.getCount();i++){

            lstevents.add(listRepository.criaevento(evento));
            evento.moveToNext();

        }
        GridLayoutManager llm = new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false);
        recycler.setHasFixedSize(true);
        System.out.println(lstevents.size());
        listRepository.fecha();

        RecyclerAdapterFavorites myadapter = new RecyclerAdapterFavorites(getActivity(),lstevents);
        recycler.setAdapter(myadapter);
        recycler.setLayoutManager(llm);
        return view;
    }

}
