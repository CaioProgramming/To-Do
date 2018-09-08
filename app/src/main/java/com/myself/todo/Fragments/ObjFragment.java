package com.myself.todo.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.myself.todo.Beans.Events;
import com.myself.todo.Database.ObjRepository;
import com.myself.todo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ObjFragment extends Fragment {

    List<Events> lstevents;
    ObjRepository objRepository;
    Spinner options;

    public ObjFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.home, container, false);
        FrameLayout frameLayout = view.findViewById(R.id.home);
        lstevents = new ArrayList<>();
        options = view.findViewById(R.id.options);
        final LinearLayout empty = view.findViewById(R.id.empty);
        final LinearLayout newev = view.findViewById(R.id.neweventform);


        objRepository = new ObjRepository(getActivity());
        objRepository.abrir();
        Intent intent = getActivity().getIntent();
        final String usuario = intent.getExtras().getString("usuario");


        options.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        getChildFragmentManager()
                                .beginTransaction()
                                .replace(R.id.objectiveframe, new NewEvent())
                                .commit();

                        break;
                    case 1:
                        getChildFragmentManager()
                                .beginTransaction()
                                .replace(R.id.objectiveframe, new BlankFragment())
                                .commit();

                        break;
                    case 2:
                        getChildFragmentManager()
                                .beginTransaction()
                                .replace(R.id.objectiveframe, new NextEvents())
                                .commit();

                        break;
                    case 3:
                        getChildFragmentManager()
                                .beginTransaction()
                                .replace(R.id.objectiveframe, new SuccesEvents())
                                .commit();

                        break;

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        return view;


    }


}
