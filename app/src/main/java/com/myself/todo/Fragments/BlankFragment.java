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
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.myself.todo.Adapters.RecyclerAdapter;
import com.myself.todo.Beans.Events;
import com.myself.todo.Database.ObjRepository;
import com.myself.todo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class BlankFragment extends Fragment {

    List<Events> lstevents;
    ObjRepository objRepository;

    public BlankFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.events, container, false);
        FrameLayout frameLayout = view.findViewById(R.id.events);
        lstevents = new ArrayList<>();

        RecyclerView recycler = view.findViewById(R.id.recyclerviewevents);
        LinearLayout empty = view.findViewById(R.id.noevents);
        objRepository = new ObjRepository(getActivity());
        objRepository.abrir();
        Intent intent = getActivity().getIntent();

        String usuario = intent.getExtras().getString("usuario");
        Cursor evento = objRepository.obterEventos(usuario);
        System.out.println(evento.getCount());
        evento.moveToFirst();

        if (evento.getCount() == 0) {

        }else {

            empty.setVisibility(View.INVISIBLE);
            recycler.setVisibility(View.VISIBLE);

        }
        for (int i = 0 ;i < evento.getCount();i++){

            lstevents.add(objRepository.criaevento(evento));
            evento.moveToNext();

        }
        GridLayoutManager llm = new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false);
        recycler.setHasFixedSize(true);
        System.out.println(lstevents.size());
        objRepository.fecha();

        RecyclerAdapter myadapter = new RecyclerAdapter(getActivity(),lstevents);
        recycler.setAdapter(myadapter);
        recycler.setLayoutManager(llm);
        return view;

        
    }



}
