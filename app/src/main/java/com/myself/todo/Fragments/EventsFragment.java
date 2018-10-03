package com.myself.todo.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.github.mmin18.widget.RealtimeBlurView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.myself.todo.Adapters.RecyclerAdapter;
import com.myself.todo.Beans.Events;
import com.myself.todo.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import de.mateware.snacky.Snacky;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventsFragment extends Fragment {

    List<Events> lstevents;
    FrameLayout events;
    LinearLayout neweventform;
    EditText neweventdescription;
    EditText neweventtext;
    Button newbtn;
    RealtimeBlurView blur;
    RecyclerView recyclerviewevents;
    Switch successwitch;
    Switch favoriteswitch;
    LinearLayout noevents;
    LinearLayout content;
    Button eventform;
    DatabaseReference raiz;


    public EventsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = Objects.requireNonNull(inflater).inflate(R.layout.events, container, false);
        raiz = FirebaseDatabase.getInstance().getReference("events");
        lstevents = new ArrayList<>();
        TextView voltar = view.findViewById(R.id.voltar);
        content = view.findViewById(R.id.content);
        events = view.findViewById(R.id.events);
        neweventform = view.findViewById(R.id.neweventform);
        newbtn = view.findViewById(R.id.newbtn);
        eventform = view.findViewById(R.id.eventform);
        neweventdescription = view.findViewById(R.id.neweventdescription);
        neweventtext = view.findViewById(R.id.neweventtext);
        blur = view.findViewById(R.id.blur);
        recyclerviewevents = view.findViewById(R.id.recyclerviewevents);
        successwitch = view.findViewById(R.id.successwitch);
        favoriteswitch = view.findViewById(R.id.favoriteswitch);
        noevents = view.findViewById(R.id.noevents);
        newbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SalvarEvento();
            }
        });

        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HideEventform();
            }
        });

        eventform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Showform();
            }
        });
        Carregar(recyclerviewevents, noevents);

        successwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (successwitch.isChecked()) {
                    Carregar(recyclerviewevents, noevents);
                    favoriteswitch.setChecked(false);
                } else {
                    CarregarConcluidos(recyclerviewevents, noevents);
                    favoriteswitch.setChecked(false);
                }
            }
        });

        favoriteswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (favoriteswitch.isChecked()) {
                    Carregar(recyclerviewevents, noevents);
                    successwitch.setChecked(false);
                } else {
                    CarregarFavoritos(recyclerviewevents, noevents);
                    successwitch.setChecked(false);
                }
            }
        });


        return view;


    }

    private void SalvarEvento() {
        String event = neweventtext.getText().toString();
        String desc = neweventdescription.getText().toString();
        Date datenow = Calendar.getInstance().getTime();
        String dia = String.valueOf(datenow);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (!TextUtils.isEmpty(event) && (!TextUtils.isEmpty(desc))) {
            String id = raiz.push().getKey();

            Events e = null;
            if (user != null) {
                e = new Events(id, neweventtext.getText().toString(), neweventdescription.getText().toString(), user.getUid(), dia, "N");
            }
            if (id != null) {
                raiz.child(id).setValue(e).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Snacky.builder().setActivity(Objects.requireNonNull(getActivity())).success().setText("Evento adicionado com sucesso").show();
                            HideEventform();

                        } else {
                            Snacky.builder().setActivity(Objects.requireNonNull(getActivity())).error().setText("Erro " + task.getException()).show();
                        }
                    }
                });
            }


        }
        Carregar(recyclerviewevents, noevents);
    }


    private void Carregar(final RecyclerView recycler, final LinearLayout empty) {
        lstevents.clear();
        recycler.clearOnChildAttachStateChangeListeners();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference raiz = FirebaseDatabase.getInstance().getReference().child("events");
        raiz.keepSynced(true);
        if (user != null) {
            raiz.child("events").orderByChild("userID").equalTo(user.getUid());
        }
        raiz.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    Events e = d.getValue(Events.class);
                    Events events = new Events();
                    if (e != null) {
                        events.setEvento(e.getEvento());
                        events.setDescricao(e.getDescricao());
                        events.setStatus(e.getStatus());
                        events.setData(e.getData());
                        events.setId(e.getId());
                        events.setUserID(e.getUserID());
                        System.out.println(e.getStatus());
                        lstevents.add(e);
                        System.out.println(lstevents.size());
                    }


                }
                recycler.setVisibility(View.VISIBLE);
                GridLayoutManager llm = new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false);
                recycler.setHasFixedSize(true);
                System.out.println(lstevents.size());
                //final Animation myanim2 = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_bottom);
                RecyclerAdapter myadapter = new RecyclerAdapter(getContext(), getActivity(), lstevents, blur);
                recycler.setAdapter(myadapter);
                recycler.setLayoutManager(llm);
                //recycler.startAnimation(myanim2);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Snacky.builder().setActivity(Objects.requireNonNull(getActivity())).error().setText("Erro " + databaseError).show();

            }
        });

    }

    private void CarregarFavoritos(final RecyclerView recycler, final LinearLayout empty) {
        lstevents.clear();
        recycler.clearOnChildAttachStateChangeListeners();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference raiz = FirebaseDatabase.getInstance().getReference().child("events");
        raiz.keepSynced(true);
        if (user != null) {
            raiz.child("events").orderByChild("userID").equalTo(user.getUid());
        }
        raiz.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    Events e = d.getValue(Events.class);
                    Events events = new Events();
                    if (e != null) {
                        events.setEvento(e.getEvento());
                        events.setDescricao(e.getDescricao());
                        events.setStatus(e.getStatus());
                        events.setData(e.getData());
                        events.setId(e.getId());
                        events.setUserID(e.getUserID());
                        System.out.println(e.getStatus());
                        if (events.getStatus().equals("F")) {
                            lstevents.add(e);
                            System.out.println(lstevents.size());
                        }
                    }


                }
                recycler.setVisibility(View.VISIBLE);
                GridLayoutManager llm = new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false);
                recycler.setHasFixedSize(true);
                System.out.println(lstevents.size());
                //final Animation myanim2 = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_bottom);
                RecyclerAdapter myadapter = new RecyclerAdapter(getContext(), getActivity(), lstevents, blur);
                recycler.setAdapter(myadapter);
                recycler.setLayoutManager(llm);
                //recycler.startAnimation(myanim2);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Snacky.builder().setActivity(Objects.requireNonNull(getActivity())).error().setText("Erro " + databaseError).show();

            }
        });

    }

    private void CarregarConcluidos(final RecyclerView recycler, final LinearLayout empty) {
        lstevents.clear();
        recycler.clearOnChildAttachStateChangeListeners();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference raiz = FirebaseDatabase.getInstance().getReference().child("events");
        raiz.keepSynced(true);
        if (user != null) {
            raiz.child("events").orderByChild("userID").equalTo(user.getUid());
        }
        raiz.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    Events e = d.getValue(Events.class);
                    Events events = new Events();
                    if (e != null) {
                        events.setEvento(e.getEvento());
                        events.setDescricao(e.getDescricao());
                        events.setStatus(e.getStatus());
                        events.setData(e.getData());
                        events.setId(e.getId());
                        events.setUserID(e.getUserID());
                        System.out.println(e.getStatus());
                        if (events.getStatus().equals("C")) {
                            lstevents.add(e);
                            System.out.println(lstevents.size());
                        }
                    }


                }
                recycler.setVisibility(View.VISIBLE);
                GridLayoutManager llm = new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false);
                recycler.setHasFixedSize(true);
                System.out.println(lstevents.size());
                //final Animation myanim2 = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_bottom);
                RecyclerAdapter myadapter = new RecyclerAdapter(getContext(), getActivity(), lstevents, blur);
                recycler.setAdapter(myadapter);
                recycler.setLayoutManager(llm);
                //recycler.startAnimation(myanim2);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Snacky.builder().setActivity(Objects.requireNonNull(getActivity())).error().setText("Erro " + databaseError).show();

            }
        });

    }


    private void ShowEvents() {
        recyclerviewevents.setVisibility(View.VISIBLE);

        GridLayoutManager llm = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
        recyclerviewevents.setHasFixedSize(true);
        System.out.println(lstevents.size());
        if (lstevents.size() > 0) {
            noevents.setVisibility(View.INVISIBLE);
            content.setVisibility(View.VISIBLE);

        }
        final Animation myanim2 = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_bottom);
        RecyclerAdapter myadapter = new RecyclerAdapter(getContext(), getActivity(), lstevents, blur);
        recyclerviewevents.startAnimation(myanim2);
        recyclerviewevents.setAdapter(myadapter);
        recyclerviewevents.setLayoutManager(llm);
    }


    private void Showform() {
        blur.setVisibility(View.VISIBLE);
        blur.setBlurRadius(50);
        neweventform.setVisibility(View.VISIBLE);
    }

    private void HideEventform() {
        blur.setVisibility(View.INVISIBLE);
        neweventform.setVisibility(View.INVISIBLE);
        content.setVisibility(View.VISIBLE);
    }


}
