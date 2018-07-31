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
public class BlankFragment extends Fragment {

    List<Events> lstevents;
    ListRepository listRepository;

    public BlankFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.home,container,false);
        FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.home);
        lstevents = new ArrayList<>();

        RecyclerView recycler = view.findViewById(R.id.recyclervievents);
        LinearLayout empty = view.findViewById(R.id.empty);
        listRepository = new ListRepository(getActivity());
        listRepository.abrir();
        Cursor evento = listRepository.obterEventos();
        System.out.println(evento.getCount());
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

        RecyclerAdapter myadapter = new RecyclerAdapter(getActivity(),lstevents);
        recycler.setAdapter(myadapter);
        recycler.setLayoutManager(llm);
        return view;

        
    }



}
