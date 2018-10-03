package com.myself.todo.Fragments;


import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;

import com.github.mmin18.widget.RealtimeBlurView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.myself.todo.Adapters.RecyclerFotoAdapter;
import com.myself.todo.Beans.Album;
import com.myself.todo.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.mateware.snacky.Snacky;

/**
 * A simple {@link Fragment} subclass.
 */
public class FotosFragment extends Fragment {

    List<Album> lstalbum;

    Switch favorites;


    public FotosFragment() {
        // Required empty public constructor
    }

    RealtimeBlurView blur;
    ProgressBar pb;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fotos, container, false);
        FrameLayout frameLayout = view.findViewById(R.id.fotos);
        lstalbum = new ArrayList<>();
        blur = getActivity().findViewById(R.id.rootblur);
        favorites = view.findViewById(R.id.favoriteswitch);
        final RecyclerView recycler = view.findViewById(R.id.recyclerviewfotos);
        final LinearLayout empty = view.findViewById(R.id.nofotos);
        final ProgressBar pb = view.findViewById(R.id.progress);
        CarregarFotos(recycler, empty);


        favorites.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    pb.setVisibility(View.VISIBLE);
                    pb.animate().start();
                    recycler.setVisibility(View.INVISIBLE);
                    lstalbum.clear();
                    recycler.destroyDrawingCache();
                    CountDownTimer countDownTimer = new CountDownTimer(2000, 300) {
                        @Override
                        public void onTick(long l) {

                        }

                        @Override
                        public void onFinish() {
                            pb.setVisibility(View.INVISIBLE);
                            recycler.setVisibility(View.VISIBLE);
                            CarregarFotosFavoritas(recycler, empty);
                        }
                    }.start();


                } else {
                    pb.setVisibility(View.VISIBLE);
                    pb.animate().start();
                    recycler.setVisibility(View.INVISIBLE);
                    lstalbum.clear();
                    recycler.destroyDrawingCache();
                    CountDownTimer countDownTimer = new CountDownTimer(2000, 300) {
                        @Override
                        public void onTick(long l) {

                        }

                        @Override
                        public void onFinish() {
                            pb.setVisibility(View.INVISIBLE);
                            recycler.setVisibility(View.VISIBLE);
                            CarregarFotos(recycler, empty);
                        }
                    }.start();
                }
            }
        });

        return view;


    }

    private void CarregarFotos(final RecyclerView recycler, LinearLayout empty) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference raiz = FirebaseDatabase.getInstance().getReference().child("album");
        raiz.keepSynced(true);
        if (user != null) {
            raiz.child("album").orderByChild("userID").equalTo(user.getUid());
        }
        raiz.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    Album a = d.getValue(Album.class);
                    Album album = new Album();
                    if (a != null) {
                        System.out.println(a.getId());
                        System.out.println(d.getKey());
                        album.setFotouri(a.getFotouri());
                        album.setDescription(a.getDescription());
                        album.setStatus(a.getStatus());
                        album.setDia(a.getDia());
                        album.setId(d.getKey());
                        album.setUserID(a.getUserID());
                        lstalbum.add(album);
                        System.out.println(album.getId());
                        System.out.println(lstalbum.size());

                    }


                }
                recycler.setVisibility(View.VISIBLE);
                GridLayoutManager llm = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
                recycler.setHasFixedSize(true);
                System.out.println(lstalbum.size());
                //final Animation myanim2 = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_bottom);
                RecyclerFotoAdapter myadapter = new RecyclerFotoAdapter(getContext(), getActivity(), lstalbum, blur);
                recycler.setAdapter(myadapter);
                recycler.setLayoutManager(llm);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Snacky.builder().setActivity(Objects.requireNonNull(getActivity())).error().setText("Erro " + databaseError).show();

            }
        });

    }

    private void CarregarFotosFavoritas(final RecyclerView recycler, final LinearLayout empty) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference raiz = FirebaseDatabase.getInstance().getReference().child("album");
        raiz.keepSynced(true);
        if (user != null) {
            raiz.child("album").orderByChild("userID").equalTo(user.getUid());
        }
        raiz.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    Album a = d.getValue(Album.class);
                    Album album = new Album();
                    if (a != null) {
                        album.setFotouri(a.getFotouri());
                        album.setDescription(a.getDescription());
                        album.setStatus(a.getStatus());
                        album.setDia(a.getDia());
                        album.setId(a.getId());
                        album.setUserID(a.getUserID());
                        System.out.println(a.getStatus());
                        if (album.getStatus().equals("F")) {
                            lstalbum.clear();
                            lstalbum.add(a);
                            System.out.println(lstalbum.size());

                        }
                    }


                }
                recycler.setVisibility(View.VISIBLE);
                GridLayoutManager llm = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
                recycler.setHasFixedSize(true);
                System.out.println(lstalbum.size());
                //final Animation myanim2 = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_bottom);
                RecyclerFotoAdapter myadapter = new RecyclerFotoAdapter(getContext(), getActivity(), lstalbum, blur);
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


}
