package com.myself.todo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.myself.todo.Beans.Events;
import com.myself.todo.Database.ListRepository;

import de.mateware.snacky.Snacky;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
  * to handle interaction events.
 */
public class NewEvent extends Fragment {


    public NewEvent() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.newevent,container,false);
        FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.neweventframe);

        final EditText text = view.findViewById(R.id.neweventtext);
        final EditText description =view.findViewById(R.id.neweventdescription);
        Button addbtn =  view.findViewById(R.id.newbtn);
        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (text.getText().equals("") || description.getText().equals("")){
                    Snacky.builder()
                            .setActivity(getActivity())
                            .setText("Escreva nos campos! ")
                            .setDuration(Snacky.LENGTH_SHORT)
                            .error()
                            .show();
                    return;
                }else {
                 ;
                    Events events = new Events();
                    events.setEvento(String.valueOf(text.getText()));
                    events.setDescricao(String.valueOf(description.getText()));
                    ListRepository eventos = new ListRepository(getActivity());
                    eventos.abrir();
                    eventos.inserir(events.getEvento(),events.getDescricao());
                    Snacky.builder()
                            .setActivity(getActivity())
                            .setText("Evento adcionado")
                            .setDuration(Snacky.LENGTH_SHORT)
                            .success()
                            .show();

                        text.setText("");
                        description.setText("");
                }

            }
        });

        return view;


    }



}
